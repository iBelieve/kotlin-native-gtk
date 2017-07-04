package io.mspencer.gtk.xml

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root
class Repository {
    @field:Attribute
    lateinit var version: String

    @field:ElementList(inline = true)
    lateinit var includes: List<Include>

    @field:ElementList(inline = true)
    lateinit var packages: List<Package>

    @field:ElementList(inline = true)
    lateinit var namespaces: List<Namespace>
}

@Root(name = "include")
class Include {
    @field:Attribute
    lateinit var name: String

    @field:Attribute(required = false)
    var version: String? = null
}

@Root
class Package {
    @field:Attribute
    lateinit var name: String
}
