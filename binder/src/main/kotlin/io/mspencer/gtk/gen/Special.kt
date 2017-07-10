package io.mspencer.gtk.gen

val SPECIAL_CASES = mapOf(
        "g_object_new" to """
            |constructor(objectType: GType, vararg propertyNames: String) : this(g_object_new(objectType, propertyNames.first(), *propertyNames.drop(1))
            """.trimMargin(),
        "g_object_new_valist" to null,
        "g_object_newv" to null
)

val SPECIAL_WORDS = listOf("object")

//fun mapParams(parameters: List<Parameter>): Pair<List<ParameterDefinition>, List<Expression>> {
//    if (parameters.any { it.name == "argc" } && parameters.any { it.name == "argv" }) {
//        val params = parameters
//                .filterNot { it.name == "argc" }
//                .map { it.definition }
//                .map {
//                    if (it.name == "argv") {
//                        ParameterDefinition("args", KotlinType("Array<String>"))
//                    } else {
//                        it
//                    }
//                }
//
//        val vars = parameters
//                .map { it.variable }
//                .map {
//                    if (it.name == "argc") {
//
//                    }
//                }
//    } else {
//        return Pair(parameters.map { it.definition }, parameters.map { it.variable })
//    }
//}