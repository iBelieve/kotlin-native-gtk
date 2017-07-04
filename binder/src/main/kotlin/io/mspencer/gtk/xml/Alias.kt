package io.mspencer.gtk.xml

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root(name = "alias")
class Alias : Entry() {
    @field:Attribute(name = "type")
    lateinit var cType: String

    @field:Element
    lateinit var type: Type
}