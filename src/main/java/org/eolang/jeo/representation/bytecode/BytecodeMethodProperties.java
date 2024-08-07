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
package org.eolang.jeo.representation.bytecode;

import com.jcabi.log.Logger;
import java.util.Optional;
import java.util.stream.IntStream;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.eolang.jeo.representation.JavaName;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Bytecode method properties.
 * @since 0.1
 */
@EqualsAndHashCode
@ToString
public final class BytecodeMethodProperties implements Testable {

    /**
     * Access modifiers.
     */
    private final int access;

    /**
     * Method name.
     */
    private final String name;

    /**
     * Method descriptor.
     */
    private final String descriptor;

    /**
     * Method signature.
     */
    private final String signature;

    /**
     * Method parameters.
     */
    private final BytecodeParameters parameters;

    /**
     * Method exceptions.
     */
    private final String[] exceptions;

    /**
     * Constructor.
     * @param name Method name.
     * @param descriptor Method descriptor.
     * @param modifiers Access modifiers.
     */
    public BytecodeMethodProperties(
        final String name,
        final String descriptor,
        final int... modifiers
    ) {
        this(name, descriptor, null, modifiers);
    }

    /**
     * Constructor.
     * @param name Method name.
     * @param descriptor Method descriptor.
     * @param signature Method signature.
     * @param modifiers Access modifiers.
     * @checkstyle ParameterNumberCheck (5 lines)
     */
    public BytecodeMethodProperties(
        final String name,
        final String descriptor,
        final String signature,
        final int... modifiers
    ) {
        this(IntStream.of(modifiers).sum(), name, descriptor, signature, new String[0]);
    }

    /**
     * Constructor.
     * @param access Access modifiers.
     * @param name Method name.
     * @param descriptor Method descriptor.
     * @param signature Method signature.
     * @param exceptions Method exceptions.
     * @checkstyle ParameterNumberCheck (5 lines)
     */
    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    public BytecodeMethodProperties(
        final int access,
        final String name,
        final String descriptor,
        final String signature,
        final String... exceptions
    ) {
        this(access, name, descriptor, signature, new BytecodeParameters(), exceptions);
    }

    /**
     * Constructor.
     * @param access Access modifiers.
     * @param name Method name.
     * @param descriptor Method descriptor.
     * @param signature Method signature.
     * @param parameters Method parameters.
     * @param exceptions Method exceptions.
     * @checkstyle ParameterNumberCheck (5 lines)
     */
    public BytecodeMethodProperties(
        final int access,
        final String name,
        final String descriptor,
        final String signature,
        final BytecodeParameters parameters,
        final String... exceptions
    ) {
        this.access = access;
        this.name = name;
        this.descriptor = descriptor;
        this.signature = signature;
        this.parameters = parameters;
        this.exceptions = exceptions.clone();
    }

    @Override
    public String testCode() {
        return String.format("\"%s\", \"%s\", %d", this.name, this.descriptor, this.access);
    }

    /**
     * Is method abstract.
     * @return True if the method is abstract.
     */
    public boolean isAbstract() {
        return (this.access & Opcodes.ACC_ABSTRACT) != 0;
    }

    /**
     * Add method to a class writer.
     * @param writer Class writer.
     * @param compute If frames should be computed.
     * @return Method visitor.
     */
    MethodVisitor writeMethod(final CustomClassWriter writer, final boolean compute) {
        Logger.debug(
            this,
            String.format("Creating method visitor with the following properties %s", this)
        );
        final MethodVisitor visitor = writer.visitMethod(
            this.access,
            new JavaName(this.name).decode(),
            this.descriptor,
            Optional.ofNullable(this.signature).filter(s -> !s.isEmpty())
                .orElse(null),
            this.exceptions,
            compute
        );
        this.parameters.write(visitor);
        return visitor;
    }
}
