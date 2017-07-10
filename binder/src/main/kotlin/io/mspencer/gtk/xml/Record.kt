package io.mspencer.gtk.xml

import io.mspencer.gtk.ast.*
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root(name = "record")
class Record : TypedEntry(), ClassLike {
    @field:Attribute(required = false)
    var disguised: Boolean = false

    @field:Attribute(name = "symbol-prefix", required = false)
    var cPrefix: String? = null

    @field:Attribute(name = "is-gtype-struct-for", required = false)
    var typeStructFor: String? = null

    @field:ElementList(inline = true, required = false, empty = false)
    override lateinit var constructors: List<Constructor>

    @field:ElementList(inline = true, required = false, empty = false)
    override lateinit var fields: List<Field>

    @field:ElementList(inline = true, required = false, empty = false)
    override lateinit var unions: List<Union>

    @field:ElementList(inline = true, required = false, empty = false)
    override lateinit var methods: List<Method>

    @field:ElementList(inline = true, required = false, empty = false)
    override lateinit var functions: List<Function>

    override val virtualMethods: List<VirtualMethod> = listOf()
    override val properties: List<PropertyNode> = listOf()
    override val signals: List<Signal> = listOf()

    val isSimpleStruct get() = constructors.isEmpty() && functions.none { it.name == "new" }

    override val definition by lazy {
        if (isSimpleStruct) {
            val params = fields.map {
                ParameterDefinition(it.name!!.toCamelCase(), it.type, it.nullable, optional = it.nullable,
                        prefix = "val", defaultValue = it.type.defaultValue)
            }
            val type = Type()
            type.name = name
            type.cType = cType
            val fillMappings = fields.map {
                val source = TypedVariable(it.name!!.toCamelCase(), it.type, it.nullable)
                val target = TypedVariable("struct.${it.name}", it.type, it.nullable)

                Assignment(target, source.toC(stringToPointer = true))
            }
            val fromMappings = fields.map {
                val source = TypedVariable("struct.${it.name}", it.type, it.nullable)
                val target = TypedVariable(it.name!!.toCamelCase(), it.type, it.nullable)

                Assignment(target, source.fromC())
            }
            val fillMethod = FunctionDefinition("fill", listOf(ParameterDefinition("struct", KotlinType(type.toString(true)))),
                    TypeDefinition(KotlinType("Unit")), MultiStatements(*fillMappings.toTypedArray()), prefix = "override")
            val fromMethod = FunctionDefinition("from", listOf(ParameterDefinition("struct", KotlinType(type.toString(true)))),
                    TypeDefinition(type), ReturnStatement(FunctionCall(name!!, *fromMappings.toTypedArray())),
                    prefix = "override")
            val companionObject = CompanionObjectDefinition(listOf(fromMethod),
                    implements = listOf("StructCompanion<$cType, $name>"))
            val notFieldMembers = memberDefinitions.filterNot { it is PropertyDefinition } + fillMethod + companionObject
            ClassDefinition(name!!, params, null, listOf("Struct<$cType>"), notFieldMembers, prefix = "data")
        } else {
            val pointer = ParameterDefinition("ptr", KotlinType("CPointer<$cType>"), prefix = "val")
            val pointerExtension = SimpleDefinition("val $name.pointer get() = ptr")

            ClassDefinition(name!!, listOf(pointer), null, listOf(), memberDefinitions,
                    extensions = listOf(pointerExtension))
        }
    }
}