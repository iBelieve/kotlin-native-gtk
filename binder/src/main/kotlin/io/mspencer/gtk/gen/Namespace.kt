package io.mspencer.gtk.gen

import io.mspencer.gtk.xml.Namespace
import io.mspencer.gtk.xml.TypedEntry
import java.io.File

/**
 * Created by Michael Spencer on 7/2/17.
 */
fun Namespace.writeToFolder(folder: File) {
    val packageName = name.toLowerCase()
    val header = "package $packageName\n\nimport kotlinx.cinterop.*\n\n"

    val packageFolder = File(folder, packageName)
    packageFolder.mkdirs()

    classes.forEach {
        File(packageFolder, "${it.name}.kt").writeText(header + it.generate())
    }
}