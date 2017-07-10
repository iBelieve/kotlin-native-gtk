package io.mspencer.gtk.xml

import io.mspencer.gtk.ast.ConstructorDefinition
import io.mspencer.gtk.ast.Definable
import io.mspencer.gtk.ast.FunctionCall
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root(name = "constructor")
class Constructor : Entry(), Definable {
    @field:Attribute(required = false)
    var throws: Boolean = false

    @field:Attribute(name = "identifier")
    lateinit var cIdentifier: String

    @field:ElementList(required = false, empty = false)
    lateinit var parameters: List<Parameter>

    @field:Element(name = "return-value")
    lateinit var returnType: ReturnValue

    override val definition: ConstructorDefinition get() {
        val call = FunctionCall(cIdentifier, *parameters.map { it.variable.toC() }.toTypedArray()).force()

        return ConstructorDefinition(parameters.map { it.definition }, listOf(call) /*, docs = documentation */)
    }
}