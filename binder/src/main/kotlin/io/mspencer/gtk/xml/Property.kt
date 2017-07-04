package io.mspencer.gtk.xml

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementUnion
import org.simpleframework.xml.Root

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root(name = "property")
class PropertyNode : Entry() {
    @field:Attribute(required = false)
    var stability: String? = null

    @field:Attribute(required = false)
    var writable: Boolean = false

    @field:Attribute(required = false)
    var readable: Boolean = false

    @field:Attribute(required = false)
    var construct: Boolean = false

    @field:Attribute(name = "construct-only", required = false)
    var constructOnly: Boolean = false

    @field:Attribute(name="transfer-ownership")
    lateinit var transferOwnership: String

    @field:ElementUnion(
            Element(name = "type", type = Type::class),
            Element(name = "array", type = Array::class))
    var type: AnyType? = null
}