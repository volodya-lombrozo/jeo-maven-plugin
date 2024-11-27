/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2024 Objectionary.com
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
package org.eolang.jeo.representation;

import com.jcabi.xml.XML;
import java.nio.file.Path;
import java.util.Arrays;
import lombok.ToString;
import org.cactoos.Input;
import org.cactoos.bytes.BytesOf;
import org.cactoos.io.InputOf;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Synced;
import org.cactoos.scalar.Unchecked;
import org.eolang.jeo.representation.asm.AsmProgram;
import org.eolang.jeo.representation.asm.DisassembleMode;
import org.eolang.jeo.representation.bytecode.Bytecode;
import org.eolang.jeo.representation.directives.DirectivesProgram;
import org.objectweb.asm.ClassReader;
import org.xembly.ImpossibleModificationException;

/**
 * Intermediate representation of a class files which can be optimized from bytecode.
 * @since 0.1
 */
@ToString
@SuppressWarnings("PMD.UseObjectForClearerAPI")
public final class BytecodeRepresentation {

    /**
     * Input source.
     */
    private final Unchecked<byte[]> input;

    /**
     * Constructor.
     * @param clazz Path to the class file
     */
    public BytecodeRepresentation(final Path clazz) {
        this(BytecodeRepresentation.fromFile(clazz));
    }

    /**
     * Constructor.
     * @param bytecode Bytecode
     */
    public BytecodeRepresentation(final Bytecode bytecode) {
        this(BytecodeRepresentation.fromBytes(bytecode.bytes()));
    }

    /**
     * Constructor.
     * @param input Input source
     */
    BytecodeRepresentation(final Input input) {
        this(BytecodeRepresentation.fromInput(input));
    }

    /**
     * Constructor.
     * @param input Input.
     */
    private BytecodeRepresentation(final Unchecked<byte[]> input) {
        this.input = input;
    }

    /**
     * Read class name from bytecode.
     *
     * @return Class name.
     */
    public String name() {
        final ClassNameVisitor name = new ClassNameVisitor();
        new ClassReader(this.input.value()).accept(name, 0);
        return name.asString();
    }

    /**
     * Convert to EOlang XML representation (XMIR).
     * @return XML.
     */
    public XML toEO() {
        return this.toEO(DisassembleMode.SHORT);
    }

    /**
     * Converts bytecode into XML.
     * @param mode Disassemble mode.
     * @return XML representation of bytecode.
     */
    public XML toEO(final DisassembleMode mode) {
        final DirectivesProgram directives = new AsmProgram(this.input.value())
            .bytecode(mode.asmOptions())
            .directives(new BytecodeListing(this.input.value()).toString());
        try {
            return new MeasuredEo(new VerifiedEo(directives)).asXml();
        } catch (final IllegalStateException exception) {
            throw new IllegalStateException(
                String.format(
                    "Something went wrong during transformation %s into XML by using directives %n%s%n",
                    this.name(),
                    directives
                ),
                exception
            );
        } catch (final ImpossibleModificationException exception) {
            throw new IllegalStateException(
                String.format(
                    "Can't build XML from %s by using directives %s",
                    Arrays.toString(this.input.value()),
                    directives
                ),
                exception
            );
        }
    }

    /**
     * Prestructor that converts a file to a byte source.
     * @param path Path to the file.
     * @return Byte source.
     */
    private static Unchecked<byte[]> fromFile(final Path path) {
        return BytecodeRepresentation.fromInput(new InputOf(path));
    }

    /**
     * Prestructor that converts input to a byte source.
     * @param input Input.
     * @return Byte source.
     */
    private static Unchecked<byte[]> fromInput(final Input input) {
        return new Unchecked<>(new Synced<>(new Sticky<>(() -> new BytesOf(input).asBytes())));
    }

    /**
     * Prestructor that converts bytes to a byte source.
     * @param bytes Bytes.
     * @return Byte source.
     */
    private static Unchecked<byte[]> fromBytes(final byte[] bytes) {
        return new Unchecked<>(new Synced<>(new Sticky<>(() -> bytes)));
    }
}
