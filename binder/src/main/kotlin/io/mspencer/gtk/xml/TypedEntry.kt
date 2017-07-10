package io.mspencer.gtk.xml

import io.mspencer.gtk.ast.Definable
import org.simpleframework.xml.Attribute

/**
 * Created by Michael Spencer on 7/2/17.
 */
abstract class TypedEntry : Entry(), Definable {
    @field:Attribute(name = "type", required = false)
    var cType: String? = null

    @field:Attribute(name = "type-name", required = false)
    var typeName: String? = null

    @field:Attribute(name = "get-type", required = false)
    var getType: String? = null
}