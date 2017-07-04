package io.mspencer.gtk.gen

import io.mspencer.gtk.xml.Class
import io.mspencer.gtk.xml.Constructor
import io.mspencer.gtk.xml.Parameter

val Class.header get() = if (parent != null) {
    "$name(pointer: CPointer<$cType>) : ${parent!!.toClassName()}(pointer)"
} else {
    "$name(protected val pointer: CPointer<$cType>)"
}

fun Class.generate() = """
        class $header {
            ${constructors.map { it.generate() }.joinToString("\n\n").indent()}
        }
        """.trimIndent()

fun Constructor.generate() = """
        constructor(${parameters.map { it.generate() }.joinToString(", ")})
                : this($cIdentifier(${parameters.map { it.name.toCamelCase() }.joinToString(", ")}))
        """.trimIndent()

fun Parameter.generate() = "${name.toCamelCase()}: Any"