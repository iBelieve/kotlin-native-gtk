package io.mspencer.gtk.ast

import io.mspencer.gtk.xml.*

class KotlinType(override val name: String) : AnyType {
    override val cType = null

    override fun toString() = name
    override fun toString(cType: Boolean) = toString()
}

open class TypedExpression(val expression: Expression, val type: AnyType,
                           override val nullable: Boolean = expression.nullable) : Expression {

    fun pointer() = if (nullable) this else force()

    override fun toString() = expression.toString()

    val needsWrapping get() = toC() != this && fromC() != this

    fun toC(stringToPointer: Boolean = false): Expression = when (type) {
        is Type -> {
            when {
                type.name == "GLib.List" -> call("toGList")
                type.name == "GLib.Variant" -> call("toGVariant")
                type.name == "gboolean" -> call("toInt")
                type.name == "gchar" -> call("toByte")
                type.name == "utf8" -> {
                    if (stringToPointer) {
                        field("cstr").call("getPointer", SimpleStatement("nativeHeap")) // .cast("CPointer<ByteVarOf<Byte>>?")
                    } else {
                        this
                    }
                }
                type.isArray -> {
                    val cType = type.cType!!.subSequence("const ".length, type.cType!!.lastIndex)
                    let(Block(SimpleExpression("it")
                            .call("fillArray", SimpleExpression("allocArray<${cType}>(it.size + 1)")))).memScoped()
                }
                type.entry is Bitfield || type.entry is Enumeration -> field("value")
                type.entry is Callback -> {
                    val callback = type.entry as Callback

                    val result = if (callback.needsWrapping) {
                        var params = callback.parameters.map { it.definition?.toString(cType = true) }.filterNotNull()
                        if (callback.throws) {
                            params += "error: CPointer<CPointerVar<GError>>?"
                        }
                        val call = TypedExpression(FunctionCall("func", *callback.parameters.map { it.variable.fromC() }.toTypedArray()),
                                callback.returnValue.type, nullable = callback.returnValue.nullable)

                        let(Block(FunctionCall("staticCFunction", Block(call.toC(stringToPointer = true), params = params)),
                                params = listOf("func")))
                    } else {
                        wrap("staticCFunction")
                    }

                    if (type.cType!!.endsWith("*")) {
                        result.call("ptr")
                    } else {
                        result
                    }
                }
                type.name == "GObject.Object" || type.entry is Class || type.entry is Record ||
                        type.entry is Interface -> field("pointer")
                else -> this
            }
        }
//        is ArrayType -> "$varName.map { it.ptr }.toCValues()"
        is Varargs -> expandVarags()
        else -> this
    }

    fun fromC(): Expression = when (type) {
//        is ArrayType -> call("toArray", Block(TypedVariable("it.value!!", type.type, false).fromC()))
        is Type -> {
            when {
                type.name == "GLib.List" -> {
                    call("toList", Block(TypedVariable("it", type.types.first(), false).fromC()))
                }
                type.name == "gboolean" -> call("toBool")
                type.name == "gchar" -> call("toChar")
                type.name == "utf8" -> pointer().call("toKString")
                type.entry is Bitfield -> wrap(type.name!!)
                type.entry is Enumeration -> wrap("${type.name}.byValue")
                type.cType?.endsWith('*') ?: false -> pointer().wrap(type.name!!)
                else -> this
            }
        }
        else -> this
    }
}

class TypeDefinition(val type: AnyType, val nullable: Boolean = false) {
    override fun toString() = toString(cType = false)

    fun toString(cType: Boolean): String {
        var string = type.toString(cType)

        if (nullable) {
            if (!string.endsWith("?"))
                string += "?"
        }

        return string
    }
}

class TypedVariable(val name: String, type: AnyType, nullable: Boolean)
    : TypedExpression(SimpleExpression(name, nullable), type) {

    fun define(value: Expression) = VariableDefinition(name, value)

    fun asParameter(optional: Boolean = false, prefix: String? = null) = ParameterDefinition(name, type, nullable, optional, prefix)
}

class VariableDefinition(val name: String, val value: Expression?, val type: TypeDefinition? = null) : Definition {
    override fun toString() = if (type != null) {
        if (value != null) {
            "val $name: $type = $value"
        } else {
            "val $name: $type"
        }
    } else {
        "val $name = $value"
    }
}