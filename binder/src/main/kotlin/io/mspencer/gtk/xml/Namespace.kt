package io.mspencer.gtk.xml

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root(name = "namespace")
class Namespace {
    @field:Attribute
    lateinit var name: String

    @field:Attribute
    lateinit var version: String

    @field:Attribute(name = "shared-library")
    lateinit var sharedLibrary: String

    @field:Attribute(name = "identifier-prefixes")
    lateinit var identifierPrefixes: String

    @field:Attribute(name = "symbol-prefixes")
    lateinit var symbolPrefixes: String

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var aliases: List<Alias>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var classes: List<Class>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var records: List<Record>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var bitfields: List<Bitfield>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var callbacks: List<Callback>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var interfaces: List<Interface>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var enums: List<Enumeration>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var constants: List<Constant>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var functions: List<Function>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var unions: List<Union>
}