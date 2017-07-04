package io.mspencer.gtk

import io.mspencer.gtk.gen.generate
import io.mspencer.gtk.gen.writeToFolder
import io.mspencer.gtk.xml.GIRPersister
import io.mspencer.gtk.xml.Repository
import java.io.File

/**
 * Created by Michael Spencer on 6/28/17.
 */
fun main(args: Array<String>) {
    val persister = GIRPersister()
    val file = File("/usr/share/gir-1.0/Gio-2.0.gir")
    val xml = persister.read(Repository::class.java, file)

    xml.namespaces.forEach {
        it.writeToFolder(File("/home/ibelieve/Developer/kotlin-native-gtk/native/src/kotlin"))
    }
}