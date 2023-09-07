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

import java.util.Arrays;
import java.util.Base64;
import org.cactoos.Input;
import org.cactoos.bytes.BytesOf;
import org.cactoos.bytes.UncheckedBytes;

/**
 * Base64 bytecode.
 * Converts bytecode to Base64 string.
 *
 * @since 0.1.0
 * @todo #37:90min Add unit test for Base64Bytecode class.
 *  The test should check all the methods of the {@link Base64Bytecode} class.
 *  Don't forget to test corner cases.
 *  When the test is ready, remove this puzzle.
 */
public class Base64Bytecode {

    /**
     * Bytecode.
     */
    private final byte[] bytes;

    /**
     * Constructor.
     * @param input Input.
     */
    Base64Bytecode(final Input input) {
        this(new UncheckedBytes(new BytesOf(input)).asBytes());
    }

    /**
     * Constructor.
     * @param bytes Bytecode.
     */
    private Base64Bytecode(final byte[] bytes) {
        this.bytes = Arrays.copyOf(bytes, bytes.length);
    }

    /**
     * Convert to string.
     * @return String.
     */
    String asString() {
        return Base64.getEncoder().encodeToString(this.bytes);
    }
}
