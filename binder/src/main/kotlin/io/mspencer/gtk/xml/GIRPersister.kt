package io.mspencer.gtk.xml

import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.transform.Matcher

/**
 * Created by Michael Spencer on 7/2/17.
 */
class GIRPersister : Persister(Matcher {
    when (it) {
        Boolean::class.java -> BooleanTransform()
        else -> null
    }
})