package io.mspencer.gtk

/**
 * Created by ibelieve on 7/1/17.
 */
class Signal<in T : Function<*>>(private val onAdd: (T) -> Unit) {
    operator fun plusAssign(listener: T) = onAdd(listener)
}

val signal = Signal<(String) -> Unit> {

}