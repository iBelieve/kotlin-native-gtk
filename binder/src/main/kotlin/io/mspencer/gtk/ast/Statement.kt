package io.mspencer.gtk.ast

import io.mspencer.gtk.xml.indent

/**
 * Created by Michael Spencer on 7/8/17.
 */
interface Statement : Expression {
    override val nullable get() = false
}

class SimpleStatement(val body: String) : Statement {
    override fun toString() = body
}

class MultiStatements(vararg val statements: Expression) : Statement {
    override fun toString() = statements.joinToString("\n")
}

class ReturnStatement(val expression: Expression) : Statement {
    override fun toString() = "return $expression"
}


class Assignment(val variable: Expression, val value: Expression) : Statement {
    override fun toString() = "$variable = $value"
}

class IfStatement(val condition: Expression, val ifTrue: Statement, val ifFalse: Statement) : Statement {
    override fun toString() = """
            |if ($condition) {
            |    ${ifTrue.toString().indent()}
            |} else {
            |    ${ifFalse.toString().indent()}
            |}
            """.trimMargin()
}
