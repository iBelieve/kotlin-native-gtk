package io.mspencer.gtk.xml

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root(name = "bitfield")
class Bitfield : TypedEntry() {
    @field:ElementList(inline = true)
    lateinit var members: List<Member>
}