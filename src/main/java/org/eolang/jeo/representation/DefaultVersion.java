/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2025 Objectionary.com
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

import org.objectweb.asm.Opcodes;

/**
 * Version of ASM and Java bytecode.
 * @since 0.1.0
 */
public final class DefaultVersion {

    /**
     * Java bytecode version.
     */
    private final int bcode;

    /**
     * ASM API version.
     */
    private final int asm;

    /**
     * Default constructor.
     */
    public DefaultVersion() {
        this(Opcodes.V1_8, Opcodes.ASM9);
    }

    /**
     * Constructor.
     * @param bytecode Java bytecode version.
     * @param api ASM API version.
     */
    private DefaultVersion(final int bytecode, final int api) {
        this.bcode = bytecode;
        this.asm = api;
    }

    /**
     * Java bytecode version.
     * @return Java bytecode version.
     */
    public int bytecode() {
        return this.bcode;
    }

    /**
     * ASM API version.
     * @return ASM API version.
     */
    public int api() {
        return this.asm;
    }
}
