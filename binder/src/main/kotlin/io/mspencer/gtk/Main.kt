package io.mspencer.gtk

import io.mspencer.gtk.xml.Callback
import io.mspencer.gtk.xml.Entry
import io.mspencer.gtk.xml.GIRPersister
import io.mspencer.gtk.xml.Repository
import java.io.File

var CURRENT_PACKAGE = ""
val ALL_TYPES = mutableMapOf<String, Entry>()

/**
 * Created by Michael Spencer on 6/28/17.
 */
fun main(args: Array<String>) {
    generate("GLib-2.0") {
                (it.name!!.startsWith("Option") && it !is Callback)
    }
//    generate("Gio-2.0")
}

fun generate(name: String, filter: (Entry) -> Boolean = { true }) {
    println("Processing $name...")
    val file = File("/usr/share/gir-1.0/$name.gir")

    GIRPersister().read(Repository::class.java, file).generate(filter)
}