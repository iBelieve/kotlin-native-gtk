package io.mspencer.gtk.xml

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root(name = "class")
class Class : TypedEntry() {
    @field:Attribute(name = "symbol-prefix")
    lateinit var cPrefix: String

    @field:Attribute(required = false)
    var parent: String? = null

    @field:Attribute(required = false)
    var abstract: Boolean = false

    @field:Attribute(name = "type-struct", required = false)
    var typeStruct: String? = null

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var implements: List<Implements>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var constructors: List<Constructor>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var virtualMethods: List<VirtualMethod>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var methods: List<Method>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var functions: List<Function>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var properties: List<PropertyNode>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var fields: List<Field>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var signals: List<Signal>
}

@Root(name = "implements")
class Implements {
    @field:Attribute
    lateinit var name: String
}