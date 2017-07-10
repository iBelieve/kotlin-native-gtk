package io.mspencer.gtk.xml

import io.mspencer.gtk.CURRENT_PACKAGE
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.io.File

/**
 * Created by Michael Spencer on 7/2/17.
 */
@Root
class Repository {
    @field:Attribute
    lateinit var version: String

    @field:ElementList(inline = true)
    lateinit var includes: List<Include>

    @field:ElementList(inline = true)
    lateinit var packages: List<Package>

    @field:ElementList(inline = true)
    lateinit var namespaces: List<Namespace>

    fun generate(filter: (Entry) -> Boolean = { true }) {
        namespaces.forEach {
            it.makeAvailable()
        }

        namespaces.forEach {
            CURRENT_PACKAGE = it.name.toLowerCase()
            it.writeToFolder(File("/home/ibelieve/Developer/kotlin-native-gtk/native/build/generated/kotlin"), filter)
        }
    }
}

@Root(name = "include")
class Include {
    @field:Attribute
    lateinit var name: String

    @field:Attribute(required = false)
    var version: String? = null
}

@Root
class Package {
    @field:Attribute
    lateinit var name: String
}
