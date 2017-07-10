package io.mspencer.gtk.xml

import io.mspencer.gtk.ALL_TYPES
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.io.File

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root(name = "namespace")
class Namespace {
    @field:Attribute
    lateinit var name: String

    @field:Attribute
    lateinit var version: String

    @field:Attribute(name = "shared-library")
    lateinit var sharedLibrary: String

    @field:Attribute(name = "identifier-prefixes")
    lateinit var identifierPrefixes: String

    @field:Attribute(name = "symbol-prefixes")
    lateinit var symbolPrefixes: String

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var aliases: List<Alias>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var classes: List<Class>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var records: List<Record>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var bitfields: List<Bitfield>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var callbacks: List<Callback>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var interfaces: List<Interface>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var enums: List<Enumeration>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var constants: List<Constant>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var functions: List<Function>

    @field:ElementList(inline = true, required = false, empty = false)
    lateinit var unions: List<Union>

    val standaloneTypes get() = (interfaces + classes + enums + bitfields +
            records.filter { it.typeStructFor == null && !it.name!!.endsWith("Private") })
    val allTypes get() = standaloneTypes + callbacks

    fun writeToFolder(folder: File, filter: (Entry) -> Boolean = { true }) {
        val packageName = name.toLowerCase()
        val header = "package $packageName\n\nimport kotlinx.cinterop.*\nimport gtk3.interop.*\n\n"

        val packageFolder = File(folder, packageName)
        packageFolder.mkdirs()

        aliases.filter(filter).let {
            if (it.isNotEmpty()) {
                File(packageFolder, "Aliases.kt").writeText(header + it.joinToString("\n\n"))
            }
        }

        callbacks.filter(filter).let {
            if (it.isNotEmpty()) {
                File(packageFolder, "Callbacks.kt").writeText(header + it.joinToString("\n\n"))
            }
        }

        constants.filter(filter).let {
            if (it.isNotEmpty()) {
                File(packageFolder, "Constants.kt").writeText(header + it.joinToString("\n\n"))
            }
        }

        standaloneTypes.filter(filter).forEach {
            File(packageFolder, "${it.name}.kt").writeText(header + it.definition)
        }
    }

    fun makeAvailable() {
        for (type in allTypes) {
            type.packageName = name.toLowerCase()
            ALL_TYPES[type.canonicalName] = type
        }
    }
}