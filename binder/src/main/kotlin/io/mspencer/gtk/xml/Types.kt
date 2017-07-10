package io.mspencer.gtk.xml

import io.mspencer.gtk.ALL_TYPES
import io.mspencer.gtk.CURRENT_PACKAGE
import org.simpleframework.xml.*

interface AnyType {
    val name: String?
    val cType: String?

    val defaultValue: String? get() = null

    val className get() = name!!.toClassName()

    fun toString(cType: Boolean): String
}

val AnyType.isCallback get() = (this as? Type)?.entry is Callback

abstract class BaseType : AnyType {
    @field:Attribute(required = false)
    override var name: String? = null

    @field:Attribute(name = "type", required = false)
    override var cType: String? = null

    override fun toString() = toString(cType = false)

    override fun toString(cType: Boolean) = "Any"
}

@Root(name = "array")
class ArrayType : BaseType() {
    @field:Attribute(required = false)
    var length: Int? = null

    @field:Attribute(name = "fixed-size", required = false)
    var fixedSize: Int? = null

    @field:Attribute(name = "zero-terminated", required = false)
    var zeroTerminated: String? = null

    @field:ElementUnion(
            Element(name = "type", type = Type::class),
            Element(name = "array", type = ArrayType::class))
    lateinit var type: AnyType

    // TODO: C type for array
    override fun toString(cType: Boolean) = "Array<${type.toString(cType)}>"

    override val defaultValue = "arrayOf()"
}

class Type : BaseType() {
    @field:ElementListUnion(
            ElementList(name = "type", inline = true, required = false, empty = false, type = Type::class),
            ElementList(name = "array", inline = true, required = false, empty = false, type = ArrayType::class))
    var types: List<AnyType> = mutableListOf()

    val entry get() = ALL_TYPES[className] ?: ALL_TYPES[CURRENT_PACKAGE + "." + className]

    val isArray get() = ((entry as? Record)?.isSimpleStruct ?: false) &&
            cType!!.startsWith("const ") && cType!!.endsWith("*")

    override val defaultValue: String?
        get() = when (name) {
            "GLib.List" -> "listOf()"
            "utf8" -> "\"\""
            "gchar" -> "NULL_CHAR"
            "gboolean" -> "false"
            "gint" -> "0"
            else -> when {
                entry is Enumeration -> {
                    if ((entry as Enumeration).members.any { it.name == "none" }) {
                        "$name.NONE"
                    } else {
                        null
                    }
                }
                else -> null
            }
        }

    override fun toString(cType: Boolean) = if (cType) {
        val baseType = when (name) {
            "utf8" -> "CPointer<gcharVar>?"
            "gchar" -> "Char"
            "gboolean" -> "Boolean"
            "gint" -> "Int"
            "none" -> "Unit"
            "gpointer" -> "COpaquePointer?"
            else -> {
                if (this.cType == null) {
                    "Any"
                } else if (this.cType!!.endsWith("*")) {
                    "CPointer<${this.cType!!.substring(0, this.cType!!.lastIndex)}>?"
                } else {
                    this.cType!!
                }
            }
        }

        if (types.isNotEmpty()) {
            "$baseType<${types.joinToString(", ")}>"
        } else {
            baseType
        }
    } else {
        var string = when (name) {
            "GLib.List" -> "List"
            "GLib.Variant" -> "Any"
            "utf8" -> "String"
            "gchar" -> "Char"
            "gboolean" -> "Boolean"
            "gint" -> "Int"
            "none" -> "Unit"
            "gpointer" -> "COpaquePointer"
            else -> name!!.toClassName()
        }

        if (types.isNotEmpty()) {
            string = "$string<${types.joinToString(", ")}>"
        }

        if (isArray) {
            string = "List<$string>"
        }

        string
    }
}

class Varargs : AnyType {
    override var name: String? = null
    override var cType: String? = null

    override fun toString() = toString(cType = false)
    override fun toString(cType: Boolean) = ""
}