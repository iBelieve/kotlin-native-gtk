package io.mspencer.gtk.xml

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root(name = "union")
class Union : Entry() {
    @field:Attribute(name = "type", required = false)
    var cType: String? = null

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var fields: List<Field>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var records: List<Record>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var methods: List<Method>
}