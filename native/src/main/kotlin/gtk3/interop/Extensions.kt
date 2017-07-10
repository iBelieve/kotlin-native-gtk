package gtk3.interop

import kotlinx.cinterop.*

open class EnumCompanion<T, V>(private val valueMap: Map<T, V>) {
    fun fromInt(type: T) = valueMap[type] ?: throw IllegalArgumentException("Unrecognized enum value: $type")
}

fun <T> CPointer<GList>.toList(block: (COpaquePointer) -> T): List<T?> {
    return (0..g_list_length(this)).map { g_list_nth_data(this, it)?.let(block) }
}

fun <T : CPointed> List<CPointer<T>?>.toGList(): CPointer<GList> {
    return g_list_alloc()!!.also { list ->
        forEach { g_list_append(list, it) }
    }
}

// fun <T : CVariable, R> CPointer<T>.toArray(block: (T) -> R): Array<R> {
//     val list = mutableListOf<R>()
//     var i = 0
//     while (true) {
//         val item = this[i]

//         if (item == null)
//             return list.toTypedArray()

//         list += block(item)
//     }
// }

fun Any.toGVariant() = when (this) {
    is Boolean -> g_variant_new_boolean(this.toInt())
    is Byte -> g_variant_new_byte(this)
    is Short -> g_variant_new_int16(this)
    is Int -> g_variant_new_int32(this)
    is Long -> g_variant_new_int64(this)
    is Double -> g_variant_new_double(this)
    is String -> g_variant_new_string(this)
    else -> throw IllegalArgumentException("Cannot be converted to a GVariant: $this")
}!!

fun Boolean.toInt() = if (this) 1 else 0
fun Int.toBool() = this != 0

fun Any?.toGVariant() = this?.toGVariant()

val NULL_CHAR = 0.toChar()
