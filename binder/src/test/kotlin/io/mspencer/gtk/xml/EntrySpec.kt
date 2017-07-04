package io.mspencer.gtk.xml

import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should not be null`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.properties.Delegates.notNull

/**
 * Created by Michael Spencer on 7/2/17.
 */
object EntrySpec : Spek({
    describe("a simple entry") {
        var subject by notNull<Entry>()

        beforeEachTest {
            val persister = GIRPersister()
            subject = persister.read(Entry::class.java, """
                |<?xml version="1.0"?>
                |<method name="popup_for_device"
                |        version="3.0"
                |        deprecated="1"
                |        deprecated-version="3.22">
                |  <doc xml:space="preserve">Displays a menu and makes it available for selection.</doc>
                |  <doc-deprecated xml:space="preserve">Please use gtk_menu_popup_at_widget(),
                |    gtk_menu_popup_at_pointer(). or gtk_menu_popup_at_rect() instead</doc-deprecated>
                |</method>
            """.trimMargin())
        }

        it("should have parsed correctly") {
            subject `should be instance of` Entry::class
        }

        it("should have documentation") {
            subject.documentation `should equal` "Displays a menu and makes it available for selection."
        }

        it("should be marked as deprecated") {
            subject.deprecated `should be` true
            subject.deprecatedVersion `should equal` "3.22"
            subject.deprecatedReason.`should not be null`()
        }
    }
})