package io.mspencer.gtk.xml

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root(name = "member")
class Member : Entry() {
    @field:Attribute
    lateinit var value: String

    @field:Attribute(name = "identifier")
    lateinit var cIdentifier: String

    @field:Attribute(required = false)
    var nick: String? = null
}