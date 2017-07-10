import glib.*
import kotlinx.cinterop.*
import gtk3.interop.*

fun main(args: Array<String>) {
    val context = OptionContext("FILES...")
    context.setDescription("Sample program to launch nukes at FILES")

    context.addMainEntries(listOf(
        OptionEntry(longName = "delay", shortName = 'd', description = "Delay launching the nukes so you have time to ^C")
    ), null)

    println(context.getHelp(true, null))

    context.free()
}
