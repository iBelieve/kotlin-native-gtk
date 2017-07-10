package io.mspencer.gtk.xml

import io.mspencer.gtk.ast.CompanionObjectDefinition
import io.mspencer.gtk.ast.ConstructorDefinition
import io.mspencer.gtk.ast.Definition

/**
 * Created by Michael Spencer on 7/9/17.
 */
interface ClassLike {
    val constructors: List<Constructor>
    val virtualMethods: List<VirtualMethod>
    val methods: List<Method>
    val functions: List<Function>
    val properties: List<PropertyNode>
    val fields: List<Field>
    val unions: List<Union>
    val signals: List<Signal>

    val memberDefinitions: List<Definition> get() {
        var members: List<Definition> = constructors.map { it.definition } +
                functions.map { it.definition }.filterIsInstance<ConstructorDefinition>() +
                fields.map { it.definition } +
                methods.filterNot { it.parameters.any { it.direction != null || it.type.isCallback } }.map { it.definition }
        val staticMembers = functions.map { it.definition }.filterNot { it is ConstructorDefinition }

        if (staticMembers.isNotEmpty()) {
            members += CompanionObjectDefinition(staticMembers)
        }

        return members
    }
}