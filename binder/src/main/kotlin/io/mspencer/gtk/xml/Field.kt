package io.mspencer.gtk.xml

import io.mspencer.gtk.ast.*
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementUnion
import org.simpleframework.xml.Root

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root(name = "field")
class Field : Entry() {
    @field:Attribute(required = false)
    var bits: String? = null

    @field:Attribute(required = false)
    var readable: String? = null

    @field:Attribute(required = false)
    var writable: String? = null

    @field:Attribute(required = false)
    var private: String? = null

    @field:ElementUnion(
            Element(name = "type", type = Type::class),
            Element(name = "array", type = ArrayType::class),
            Element(name = "callback", type = Callback::class))
    lateinit var type: AnyType

    val nullable get() = (type.cType?.endsWith("*") ?: false) || type.name == "gpointer"

    val definition by lazy {
        val value = TypedVariable("value", type, nullable)
        val target = TypedVariable("ptr.pointed.$name", type, nullable)

        val setter = if (type.name == "utf8") {
            MultiStatements(target.wrap("nativeHeap.free"),
                    Assignment(target, value.toC(stringToPointer = true)))
        } else {
            Assignment(target, value.toC())
        }

        PropertyDefinition(name!!.toCamelCase(), TypeDefinition(type, nullable),
                setter, ReturnStatement(target.fromC()))
    }
}