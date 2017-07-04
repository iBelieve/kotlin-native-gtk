package io.mspencer.gtk.gen

/**
 * Created by Michael Spencer on 7/2/17.
 */
fun String.indent(indent: String = "    ") = replace("\n", "\n$indent")

fun String.toClassName() = split(".")
        .reversed()
        .mapIndexed { index, it -> if (index == 0) it else it.toLowerCase() }
        .reversed()
        .joinToString(".")

fun String.toCamelCase(first: Boolean = false) = split("_").dropLastWhile { it.isEmpty() }
        .mapIndexed { index, it -> if (index > 0 || first) it.toProperCase() else it }
        .joinToString("")

fun String.toProperCase() = substring(0, 1).toUpperCase() + substring(1).toLowerCase()