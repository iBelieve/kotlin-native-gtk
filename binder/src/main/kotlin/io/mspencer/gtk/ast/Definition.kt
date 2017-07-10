package io.mspencer.gtk.ast

import io.mspencer.gtk.xml.AnyType
import io.mspencer.gtk.xml.indent

fun docString(docs: String?) = docs?.let {
    "/**\n${it.lines().map { " * $it" }.joinToString("\n")}\n */\n"
} ?: ""

interface Definition : Statement

interface Definable {
    val definition: Definition
}

class SimpleDefinition(val body: String) : Definition {
    override fun toString() = body
}

class EmptyDefinition : Definition {
    override fun toString() = ""
}

class ParameterDefinition(val name: String, type: AnyType, nullable: Boolean = false,
                          val optional: Boolean = false, val prefix: String? = null, val defaultValue: String? = null) {
    val typeDefinition = TypeDefinition(type, nullable)

    override fun toString() = toString(cType = false)

    fun toString(cType: Boolean): String {
        val string = if (prefix != null) {
            "$prefix $name: ${typeDefinition.toString(cType)}"
        } else {
            "$name: ${typeDefinition.toString(cType)}"
        }

        return if (optional) {
            "$string = null"
        } else if (defaultValue != null) {
            "$string = $defaultValue"
        } else {
            string
        }
    }
}

class FunctionDefinition(val name: String, parameters: List<ParameterDefinition?>, val returnType: TypeDefinition,
                         val body: Expression, val docs: String? = null, val prefix: String? = null) : Definition {
    val parameters = parameters.filterNotNull()

    override fun toString(): String {
        val returnString = returnType.toString()
                .let {
                    if (it == "Unit") {
                        ""
                    } else {
                        ": $it"
                    }
                }

        var header = docString(docs)

        if (prefix != null)
            header += prefix + " "

        return """
                |${header}fun $name(${parameters.joinToString(", ")})$returnString {
                |    ${body.toString().indent()}
                |}""".trimMargin()
    }
}

class ConstructorDefinition(parameters: List<ParameterDefinition?>, val thisParameters: List<Expression>,
                            val body: Statement? = null, val docs: String? = null) : Definition {
    val parameters = parameters.filterNotNull()

    override fun toString(): String {
        val cons = "${docString(docs)}constructor(${parameters.joinToString(", ")}) : " +
                "this(${thisParameters.joinToString(", ")})"

        return if (body != null) {
            """
            |$cons {
            |    ${body.toString().indent()}
            |}
            """.trimMargin()
        } else {
            cons
        }
    }
}

class ClassDefinition(val name: String, val parameters: List<ParameterDefinition>,
                      val parent: FunctionCall?, val implements: List<String>,
                      val members: List<Definition>, val prefix: String? = null,
                      val extensions: List<Definition> = listOf()) : Definition {
    private val extendsString: String get() {
        var list = implements

        if (parent != null) {
            list = listOf(parent.toString()) + implements
        }

        if (list.isNotEmpty()) {
            return ": " + list.joinToString(", ")
        } else {
            return ""
        }
    }

    override fun toString(): String {
        val constructor = if (parameters.isNotEmpty()) {
            "(" + parameters.joinToString(", ") + ")"
        } else {
            ""
        }

        var string = """
                |class $name$constructor$extendsString {
                |    ${members.joinToString("\n\n").indent()}
                |}
                """.trimMargin()

        if (extensions.isNotEmpty()) {
            string += "\n\n" + extensions.joinToString("\n\n")
        }

        return if (prefix != null) {
            "$prefix $string"
        } else {
            string
        }
    }
}

class InterfaceDefinition(val name: String, val implements: List<String>,
                          val members: List<Definition>) : Definition {
    private val extendsString: String get() {
        if (implements.isNotEmpty()) {
            return ": " + implements.joinToString(", ")
        } else {
            return ""
        }
    }

    override fun toString() = """
                |interface $name$extendsString {
                |    ${members.joinToString("\n\n").indent()}
                |}
                """.trimMargin()
}

class IntEnumDefinition(val name: String, val type: String, val members: List<EnumMemberDefinition>) : Definition {
    override fun toString() = """
                |enum class $name(val value: $type) {
                |    ${members.joinToString(",\n").indent()};
                |
                |    companion object {
                |       fun byValue(value: $type) = $name.values().find { it.value == value }!!
                |    }
                |}
                """.trimMargin()
}

class EnumMemberDefinition(val name: String, val value: String) : Definition {
    override fun toString() = "$name($value)"
}

class CompanionObjectDefinition(val members: List<Definition>, val parent: FunctionCall? = null,
                                val implements: List<String> = listOf()) : Definition {

    private val extendsString: String get() {
        var list = implements

        if (parent != null) {
            list = listOf(parent.toString()) + implements
        }

        if (list.isNotEmpty()) {
            return ": " + list.joinToString(", ")
        } else {
            return ""
        }
    }

    override fun toString() = """
            |companion object $extendsString {
            |    ${members.joinToString("\n\n").indent()}
            |}
            """.trimMargin()
}

class PropertyDefinition(val name: String, val type: TypeDefinition, val setter: Expression,
                         val getter: Expression) : Definition {
    override fun toString() = """
            |var $name: $type
            |    set(value) {
            |        ${setter.toString().indent(2)}
            |    }
            |    get() {
            |        ${getter.toString().indent(2)}
            |    }
            """.trimMargin()
}