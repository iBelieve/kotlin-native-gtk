package io.mspencer.gtk.xml

import io.mspencer.gtk.ast.*
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root(name = "bitfield")
class Bitfield : TypedEntry() {
    @field:ElementList(inline = true)
    lateinit var members: List<Member>

    override val definition by lazy {
        val valueParam = ParameterDefinition("value", KotlinType("Int"), prefix = "val")
        ClassDefinition(name!!, listOf(valueParam), null, listOf(),
                listOf(CompanionObjectDefinition(members.map { it.bitfieldDefinition })))
    }

    val Member.bitfieldDefinition: VariableDefinition
        get() {
            return VariableDefinition(name!!.toUpperCase(), FunctionCall(this@Bitfield.name!!, SimpleExpression(value)))
        }
}