package gobject

import kotlinx.cinterop.*
import gtk3.interop.*

abstract class Object(private val ptr: CPointer<GObject>) {

}

// val Object.pointer get() = ptr

// inline fun <T : Object> List<T>.toGList(): CPointer<GList> {
//     return g_list_alloc()!!.also { list ->
//         forEach { g_list_append(list, it.pointer) }
//     }
// }
