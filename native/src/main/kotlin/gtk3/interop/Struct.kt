package gtk3.interop

import kotlinx.cinterop.*

interface Struct<T> {
    fun fill(struct: T)
}

interface StructCompanion<T, R> {
    fun from(struct: T): R
}

fun <S : Struct<T>, T : CStructVar> List<S>.fillArray(array: CPointer<T>): CPointer<T> {
    for (i in 0 until size) {
        this[i].fill(array[i])
    }
    return array
}
