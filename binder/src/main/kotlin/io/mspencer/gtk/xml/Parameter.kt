package io.mspencer.gtk.xml

import io.mspencer.gtk.ast.KotlinType
import io.mspencer.gtk.ast.TypeDefinition
import io.mspencer.gtk.ast.TypedVariable
import org.simpleframework.xml.*

/**
 * Created by Michael Spencer on 7/2/17.
 */
class Parameters {
    @field:Element(name = "instance-parameter", required = false)
    var self: Parameter? = null

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var parameters: List<Parameter>
}

@Root(name = "parameter")
class Parameter {
    @field:Attribute
    lateinit var name: String

    @field:Attribute(required = false)
    var nullable: Boolean = false

    @field:Attribute(required = false)
    var optional: Boolean = false

    @field:Attribute(name = "allow-none", required = false)
    var allowNone: Boolean = false

    @field:Attribute(required = false)
    var scope: Boolean = false

    @field:Attribute(required = false)
    var closure: Int? = null

    @field:Attribute(required = false)
    var destroy: Int? = null

    @field:Attribute(name = "caller-allocates", required = false)
    var callerAllocates: Boolean = false

    @field:Attribute(name = "transfer-ownership")
    lateinit var transferOwnership: String

    @field:Attribute(required = false)
    var direction: String? = null

    @field:Element(name = "doc", required = false)
    var documentation: String? = null

    @field:ElementUnion(
            Element(name = "type", type = Type::class),
            Element(name = "array", type = ArrayType::class),
            Element(name = "varargs", type = Varargs::class))
    lateinit var type: AnyType

    val variable by lazy {
        when {
            nullable && type.isCallback -> TypedVariable("null", KotlinType("Unit"), true)
            else -> TypedVariable(name.toCamelCase(), type, nullable)
        }
    }

    val typeDefinition by lazy {
        TypeDefinition(type, nullable)
    }

    val definition by lazy {
        variable.asParameter(optional).takeUnless { nullable && type.isCallback }
    }
}