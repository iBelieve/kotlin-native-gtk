package io.mspencer.gtk.ast

import io.mspencer.gtk.xml.indent

/**
 * Created by Michael Spencer on 7/8/17.
 */
interface Expression {
    val nullable: Boolean

    fun force() = ForceNotNull(this)

    fun member(member: Expression) = if (member is MemScoped) {
        DotAccess(this, member.body).memScoped()
    } else {
        DotAccess(this, member)
    }

    fun field(name: String) = member(SimpleExpression(name))
    fun call(method: String, vararg parameters: Expression) = member(FunctionCall(method, *parameters))
    fun cast(type: String) = CastExpression(this, type)
    fun chain(vararg expressions: Expression) = expressions.fold(this) { base, member -> base.member(member) }

    fun let(block: Block) = member(FunctionCall("let", block, nullable = block.body.nullable))
    fun memScoped() = MemScoped(this)
    fun expandVarags() = VarargsExpansion(this)
    fun wrap(name: String) = let(Block(FunctionCall(name, SimpleExpression("it"))))
}

class SimpleExpression(val name: String, override val nullable: Boolean = false) : Expression {
    override fun toString() = name
}

class DotAccess(val base: Expression, val member: Expression) : Expression {
    override val nullable = base.nullable || member.nullable

    override fun toString() = if (base.nullable) {
        "$base?.$member"
    } else {
        "$base.$member"
    }
}

class CastExpression(val base: Expression, val type: String) : Expression {
    override val nullable = false

    override fun toString() ="$base as $type"
}

class ForceNotNull(val base: Expression) : Expression {
    override val nullable = false

    override fun toString() = "$base!!"
}

class Block(val body: Expression, val params: List<String> = listOf()) : Expression {
    override val nullable = false

    override fun toString(): String {
        val paramString = if (params.isNotEmpty()) {
            "${params.joinToString(", ")} -> "
        } else {
            ""
        }
        val bodyString = body.toString()

        return if (bodyString.length > 30 || '\n' in bodyString) {
            """
            |{ $paramString
            |    ${bodyString.indent()}
            |}
            """.trimMargin()
        } else {
            "{ $paramString$bodyString }"
        }
    }
}

open class FunctionCall(val name: String, vararg val parameters: Expression, override val nullable: Boolean = false) : Expression {
    override fun toString() = if (parameters.lastOrNull() is Block) {
        val block = parameters.last()
        val params = parameters.dropLast(1)

        if (params.isNotEmpty()) {
            "$name(${params.joinToString(", ")}) $block"
        } else {
            "$name $block"
        }
    } else {
        "$name(${parameters.joinToString(", ")})"
    }
}

class VarargsExpansion(val expression: Expression) : Expression {
    override val nullable = expression.nullable

    override fun toString() = "*$expression"
}

class MemScoped(val body: Expression) : FunctionCall("memScoped", Block(body), nullable = body.nullable)