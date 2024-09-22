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
package org.eolang.jeo.representation.xmir;

import org.eolang.jeo.representation.bytecode.BytecodeAnnotation;
import org.eolang.jeo.representation.bytecode.BytecodeAnnotations;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link XmlAnnotations}.
 * @since 0.6
 */
final class XmlAnnotationsTest {

    @Test
    void parsesXmirAnnotations() {
        MatcherAssert.assertThat(
            "We expect that XMIR annotations are parsed correctly",
            new XmlAnnotations(
                new XmlNode(
                    String.join(
                        "\n",
                        "<o name='annotations'>",
                        "  <o base='org.eolang.jeo.annotation' name='annotation-1015422024-amF2YS9sYW5nL092ZXJyaWRl'>",
                        "    <o base='org.eolang.jeo.string' data='bytes'>6A 61 76 61 2F 6C 61 6E 67 2F 4F 76 65 72 72 69 64 65</o>",
                        "    <o base='org.eolang.jeo.bool' data='bytes'>01</o>",
                        "  </o>",
                        "</o>"
                    ))).bytecode(),
            Matchers.equalTo(
                new BytecodeAnnotations(
                    new BytecodeAnnotation("java/lang/Override", true)
                )
            )
        );
    }
}
