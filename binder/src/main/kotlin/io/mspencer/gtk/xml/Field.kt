package io.mspencer.gtk.xml

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementUnion
import org.simpleframework.xml.Root

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root(name = "field")
class Field : Entry() {
    @field:Attribute(required = false)
    var bits: String? = null

    @field:Attribute(required = false)
    var readable: String? = null

    @field:Attribute(required = false)
    var writable: String? = null

    @field:Attribute(required = false)
    var private: String? = null

    @field:ElementUnion(
            Element(name = "type", type = Type::class),
            Element(name = "array", type = Array::class),
            Element(name = "callback", type = Callback::class))
    var type: AnyType? = null
}