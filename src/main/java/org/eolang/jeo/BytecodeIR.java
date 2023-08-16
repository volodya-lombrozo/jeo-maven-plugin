/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2023 Objectionary.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.eolang.jeo;

import java.io.InputStream;
import java.nio.file.Path;
import lombok.ToString;
import org.cactoos.Input;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Intermediate representation of a class files which can be optimized from bytecode.
 *
 * @since 0.1.0
 * @todo #13:90min Implement BytecodeIR class.
 *  The class should implement IR interface and represent a class file.
 *  It should be able to read the class file and provide access to its bytecode.
 *  Also we have to define methods which we need from IR to provide future optimizations.
 *  When the class is ready, just remove that puzzle.
 *  Also remove SuppressWarnings annotation from the class.
 */
@ToString
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
final class BytecodeIR implements IR {

    private final Input input;

    /**
     * Constructor.
     * @param clazz Path to the class file
     */
    BytecodeIR(final Path clazz) {
        //todo
        throw new UnsupportedOperationException("Not implemented yet");
    }

    BytecodeIR(final Input input) {
        this.input = input;
    }

    public void parse() {
        try (InputStream stream = input.stream()) {
            new ClassReader(stream)
                .accept(new ClassPrinter(), 0);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private class ClassPrinter extends ClassVisitor {

        protected ClassPrinter() {
            super(Opcodes.V16);
        }

//        @Override
//        public void visit(final int version,
//            final int access,
//            final String name,
//            final String signature,
//            final String superName,
//            final String[] interfaces
//        ) {
//            System.out.printf("%s{%n", name);
//            super.visit(version, access, name, signature, superName, interfaces);
//        }
//
//        @Override
//        public MethodVisitor visitMethod(
//            final int access,
//            final String name,
//            final String descriptor,
//            final String signature,
//            final String[] exceptions
//        ) {
//            System.out.println(String.format("%s %s", descriptor, name));
//            return super.visitMethod(access, name, descriptor, signature, exceptions);
//        }
//
//        @Override
//        public void visitEnd() {
//            System.out.println("}");
//            super.visitEnd();
//        }
    }
}
