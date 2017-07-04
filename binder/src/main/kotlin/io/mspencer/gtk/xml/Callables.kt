package io.mspencer.gtk.xml

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

abstract class Callable : Entry() {
    @field:Attribute(required = false)
    var throws: Boolean = false

    @field:Element(name = "return-value")
    lateinit var returnValue: ReturnValue

    @field:Element(name = "parameters", required = false)
    protected var allParameters: Parameters? = null

    val parameters get() = allParameters?.parameters ?: listOf()
}

@Root(name = "virtual-method")
class VirtualMethod : Callable() {
    @field:Attribute(required = false)
    var invoker: String? = null

    val self get() = allParameters?.self
}

@Root(name = "method")
class Method : Callable() {
    @field:Attribute(required = false)
    var stability: String? = null

    @field:Attribute(name = "identifier")
    lateinit var cIdentifier: String

    val self get() = allParameters?.self
}

@Root(name = "function")
class Function : Callable() {
    @field:Attribute(name = "identifier")
    lateinit var cIdentifier: String
}

@Root(name = "callback")
class Callback : Callable(), AnyType {
    @field:Attribute(name = "type", required = false)
    override var cType: String? = null
}

@Root(name = "signal")
class Signal : Callable() {
    @field:Attribute(required = false)
    var stability: String? = null

    @field:Attribute(required = false)
    var `when`: String? = null

    @field:Attribute(required = false)
    var action: String? = null

    @field:Attribute(required = false)
    var detailed: Boolean = false

    @field:Attribute(name = "no-recurse", required = false)
    var noRecurse: Boolean = false

    @field:Attribute(name = "no-hooks", required = false)
    var noHooks: Boolean = false
}
