package io.mspencer.gtk.xml

import io.mspencer.gtk.ast.*
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root(name = "class")
class Class : TypedEntry(), ClassLike {
    @field:Attribute(name = "symbol-prefix")
    lateinit var cPrefix: String

    @field:Attribute(required = false)
    var parent: String? = null

    @field:Attribute(required = false)
    var abstract: Boolean = false

    @field:Attribute(required = false)
    var fundamental: Boolean = false

    @field:Attribute(name = "type-struct", required = false)
    var typeStruct: String? = null

    @field:Attribute(name = "ref-func", required = false)
    var refFunction: String? = null

    @field:Attribute(name = "unref-func", required = false)
    var unrefFunction: String? = null

    @field:Attribute(name = "set-value-func", required = false)
    var setValueFunction: String? = null

    @field:Attribute(name = "get-value-func", required = false)
    var getValueFunction: String? = null

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var implements: List<Implements>

    @field:ElementList(inline = true, required = false, empty = false)
    override lateinit var constructors: List<Constructor>

    @field:ElementList(inline = true, required = false, empty = false)
    override lateinit var virtualMethods: List<VirtualMethod>

    @field:ElementList(inline = true, required = false, empty = false)
    override lateinit var methods: List<Method>

    @field:ElementList(inline = true, required = false, empty = false)
    override lateinit var functions: List<Function>

    @field:ElementList(inline = true, required = false, empty = false)
    override lateinit var properties: List<PropertyNode>

    @field:ElementList(inline = true, required = false, empty = false)
    override lateinit var fields: List<Field>

    @field:ElementList(inline = true, required = false, empty = false)
    override lateinit var signals: List<Signal>

    override val unions: List<Union> = listOf()

    override val definition by lazy {
        val isOpen = true
        val pointer = ParameterDefinition("ptr", KotlinType("CPointer<$cType>"),
                nullable = false, prefix = "private val")
        val pointerExtension = SimpleDefinition("val $name.pointer get() = ptr")

        val parentField = if (isOpen) "parent_instance" else "parent"
        val parentCall = parent?.let {
            FunctionCall(parent!!.toClassName(), SimpleExpression("ptr").field("pointed").field(parentField).field("ptr"))
        }

        ClassDefinition(name!!, listOf(pointer), parentCall, implements.map { it.name.toClassName() },
                memberDefinitions, extensions = listOf(pointerExtension), prefix = "open")
    }
}

@Root(name = "implements")
class Implements {
    @field:Attribute
    lateinit var name: String
}