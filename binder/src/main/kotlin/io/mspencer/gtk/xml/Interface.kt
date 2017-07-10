package io.mspencer.gtk.xml

import io.mspencer.gtk.ast.*
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root(name = "interface")
class Interface : TypedEntry() {
    @field:Attribute(name = "symbol-prefix")
    lateinit var cPrefix: String

    @field:Attribute(name = "type-struct", required = false)
    var typeStruct: String? = null

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var prerequisites: List<PrerequisiteNode>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var virtualMethods: List<VirtualMethod>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var methods: List<Method>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var functions: List<Function>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var properties: List<PropertyNode>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var signals: List<Signal>

    val memberDefinitions by lazy {
        val pointer = VariableDefinition("pointer", null, type = TypeDefinition(KotlinType("CPointer<$cType>"), nullable = false))
        val members: List<Definition> = listOf(pointer) + methods.map { it.definition }

        members
    }

    override val definition by lazy {
        InterfaceDefinition(name!!, prerequisites.map { it.name }, memberDefinitions)
    }
}

@Root(name = "prerequisite")
class PrerequisiteNode {
    @field:Attribute
    lateinit var name: String
}