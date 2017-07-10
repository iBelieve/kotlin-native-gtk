package io.mspencer.gtk.xml

import io.mspencer.gtk.ast.*
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

    protected fun define(ident: String, self: Boolean): FunctionDefinition {
        var params = parameters.map { it.variable.toC() }
        if (self)
            params = listOf(SimpleExpression("ptr")) + params
        if (throws)
            params += SimpleExpression("error.ptr")

        val memScoped = params.any { it is MemScoped }
        params = params.map { (it as? MemScoped)?.body ?: it }

        var call = TypedExpression(FunctionCall(ident, *params.toTypedArray(), nullable = returnValue.nullable),
                type = returnValue.type)

        val body = if (throws) {
            val errorVar = TypedVariable("error", KotlinType("CPointerVar<GError>"), false)
            val resultVar = TypedVariable("result", returnValue.type, returnValue.nullable)

            MultiStatements(
                    errorVar.define(FunctionCall("alloc<CPointerVar<GError>>")),
                    resultVar.define(call),
                    IfStatement(SimpleExpression("error.pointed != null"),
                            SimpleStatement("throw Exception(error.pointed?.message?.toKString() ?: \"Unable to call $ident\")"),
                            ReturnStatement(resultVar.fromC()))
            ).memScoped()
        } else {
            if (memScoped)
                ReturnStatement(call.fromC().memScoped())
            else
                ReturnStatement(call.fromC())
        }

        return FunctionDefinition(name!!.toCamelCase(), parameters.map { it.definition }, returnValue.definition,
                body /*, docs = documentation*/)
    }
}

@Root(name = "virtual-method")
class VirtualMethod : Callable() {
    @field:Attribute(required = false)
    var invoker: String? = null

    val self get() = allParameters?.self
}

@Root(name = "method")
class Method : Callable(), Definable {
    @field:Attribute(required = false)
    var stability: String? = null

    @field:Attribute(name = "identifier")
    lateinit var cIdentifier: String

    val self get() = allParameters?.self

    override val definition by lazy {
        define(cIdentifier, true)
    }
}

@Root(name = "function")
class Function : Callable(), Definable {
    @field:Attribute(name = "identifier")
    lateinit var cIdentifier: String

    override val definition by lazy {
        if (name == "new") {
            val call = FunctionCall(cIdentifier, *parameters.map { it.variable.toC() }.toTypedArray()).force()

            ConstructorDefinition(parameters.map { it.definition }, listOf(call) /*, docs = documentation */)
        } else {
            define(cIdentifier, false)
        }
    }
}

@Root(name = "callback")
class Callback : Callable(), AnyType {
    @field:Attribute(name = "type", required = false)
    override var cType: String? = null

    override val className get() = name!!.toClassName()

    override fun toString(): String {
        val params = parameters.map { it.typeDefinition }
        return "typealias $name = (${params.joinToString(", ")}) -> ${returnValue.definition}"
    }

    override fun toString(cType: Boolean) = toString()

    val needsWrapping get() = throws || TypedExpression(SimpleExpression(""), returnValue.type,
            returnValue.nullable).needsWrapping || parameters.any { it.variable.needsWrapping }
    val volatile get() = !needsWrapping
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
