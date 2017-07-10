package io.mspencer.gtk.xml

import io.mspencer.gtk.ast.SimpleExpression
import io.mspencer.gtk.ast.TypeDefinition
import io.mspencer.gtk.ast.VariableDefinition
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root(name = "constant")
class Constant : Entry() {
    @field:Attribute
    lateinit var value: String

    @field:Attribute(name = "type")
    lateinit var cType: String

    @field:Element//(required = false)
    lateinit var type: Type//? = null

    val definition by lazy {
        VariableDefinition(name!!, SimpleExpression(cType), TypeDefinition(type, false))
    }

    override fun toString() = "$docString$definition"
}