package io.mspencer.gtk.xml

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element

/**
 * Created by Michael Spencer on 7/2/17.
 */
abstract class Entry {
    @field:Attribute(required = false)
    var name: String? = null

    @field:Attribute(required = false)
    var version: String? = null

    @field:Element(name = "doc", required = false)
    var documentation: String? = null

    @field:Attribute(required = false)
    var deprecated = false

    @field:Attribute(name = "deprecated-version", required = false)
    var deprecatedVersion: String? = null

    @field:Element(name = "doc-deprecated", required = false)
    var deprecatedReason: String? = null

    @field:Attribute(name = "moved-to", required = false)
    var movedTo: String? = null

    @field:Attribute(name = "shadowed-by", required = false)
    var shadowedBy: String? = null

    @field:Attribute(required = false)
    var shadows: String? = null

    @field:Attribute(required = false)
    var introspectable: Boolean = false
}