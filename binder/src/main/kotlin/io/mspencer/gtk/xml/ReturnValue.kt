package io.mspencer.gtk.xml

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementUnion

/**
 * Created by Michael Spencer on 7/2/17.
 */
class ReturnValue {
    @field:Attribute(name = "transfer-ownership", required = false)
    var transferOwnership: String? = null

    @field:Attribute(required = false)
    var nullable: Boolean = false

    @field:Element(name = "doc", required = false)
    var documentation: String? = null

    @field:ElementUnion(
            Element(name = "type", type = Type::class),
            Element(name = "array", type = Array::class))
    var type: AnyType? = null
}