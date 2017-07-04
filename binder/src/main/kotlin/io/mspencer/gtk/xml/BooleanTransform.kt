package io.mspencer.gtk.xml

import org.simpleframework.xml.transform.Transform

/**
 * Created by Michael Spencer on 7/2/17.
 */
class BooleanTransform : Transform<Boolean> {
    override fun write(value: Boolean) = if (value) "1" else "0"

    override fun read(value: String) = value.isNotBlank() && value != "0"
}