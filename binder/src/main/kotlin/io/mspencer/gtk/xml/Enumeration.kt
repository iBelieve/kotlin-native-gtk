package io.mspencer.gtk.xml

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root(name = "enumeration")
class Enumeration : TypedEntry() {
    @field:Attribute(name = "error-domain", required = false)
    var errorDomain: String? = null

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var members: List<Member>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var functions: List<Function>
}