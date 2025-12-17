package com.badlogic.ashley.core

import org.mockito.asm.ClassWriter
import org.mockito.asm.Opcodes

/**
 * Class loader allowing dynamic [Component] class definitions.
 * Useful for tests that need several different component types.
 * Adapted from [this document](https://dzone.com/articles/fully-dynamic-classes-with-asm).
 */
class ComponentClassFactory : ClassLoader() {
    /**
     * create new [Component] type
     * @param name name of the class to create
     * @return created class
     */
    fun createComponentType(name: String): Class<out Component> {
        val cw = ClassWriter(ClassWriter.COMPUTE_FRAMES)

        val interfacePath = Component::class.java.name.replace("\\.".toRegex(), "/")

        // create public class (default package) implementing Component
        cw.visit(
            Opcodes.V1_6,  // Java 1.6
            Opcodes.ACC_PUBLIC,  // public class
            name,  // package and name
            null,  // signature (null means not generic)
            "java/lang/Object",  // superclass
            arrayOf(interfacePath)
        ) // interfaces

        // create public no-arg constructor
        val con = cw.visitMethod(
            Opcodes.ACC_PUBLIC,  // public method
            "<init>",  // method name
            "()V",  // descriptor
            null,  // signature (null means not generic)
            null
        ) // exceptions (array of strings)

        // define constructor body : call super constructor (java.lang.Object)
        con.visitCode() // Start the code for this method
        con.visitVarInsn(Opcodes.ALOAD, 0) // Load "this" onto the stack
        con.visitMethodInsn(
            Opcodes.INVOKESPECIAL,  // Invoke an instance method (non-virtual)
            "java/lang/Object",  // Class on which the method is defined
            "<init>",  // Name of the method
            "()V"
        ) // Descriptor
        con.visitInsn(Opcodes.RETURN) // End the constructor method
        con.visitMaxs(1, 1) // Specify max stack and local vars

        // close class definition.
        cw.visitEnd()

        // load and return class.
        val b = cw.toByteArray()
        return defineClass(name, b, 0, b.size).asSubclass(Component::class.java)
    }
}
