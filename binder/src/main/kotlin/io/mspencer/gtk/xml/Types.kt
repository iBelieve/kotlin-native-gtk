package io.mspencer.gtk.xml

import org.simpleframework.xml.*

interface AnyType {
    val name: String?
    val cType: String?
}

abstract class BaseType : AnyType {
    @field:Attribute(required = false)
    override var name: String? = null

    @field:Attribute(name = "type", required = false)
    override var cType: String? = null
}

class Array : BaseType() {
    @field:Attribute(required = false)
    var length: Int? = null

    @field:Attribute(name = "fixed-size", required = false)
    var fixedSize: Int? = null

    @field:Attribute(name = "zero-terminated", required = false)
    var zeroTerminated: String? = null

    @field:ElementUnion(
            Element(name = "type", type = Type::class),
            Element(name = "array", type = Array::class))
    lateinit var type: AnyType
}

class Type : BaseType() {
    @field:ElementListUnion(
            ElementList(name = "type", inline = true, required = false, empty = false, type = Type::class),
            ElementList(name = "array", inline = true, required = false, empty = false, type = Array::class))
    lateinit var types: List<AnyType>
}

class Varargs : AnyType {
    override var name: String? = null
    override var cType: String? = null
}