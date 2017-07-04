package io.mspencer.gtk.xml

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root(name = "record")
class Record : TypedEntry() {
    @field:Attribute(required = false)
    var disguised: String? = null

    @field:Attribute(name = "symbol-prefix", required = false)
    var cPrefix: String? = null

    @field:Attribute(name = "is-gtype-struct-for", required = false)
    var typeStructFor: String? = null

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var constructors: List<Constructor>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var fields: List<Field>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var unions: List<Union>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var methods: List<Method>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var functions: List<Function>
}