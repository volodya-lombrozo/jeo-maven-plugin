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
package org.eolang.jeo.representation.xmir;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.eolang.jeo.representation.DataType;
import org.eolang.jeo.representation.bytecode.BytecodeAnnotation;

/**
 * XML operand.
 * @since 0.3
 */
@ToString
@EqualsAndHashCode
public final class XmlOperand {

    /**
     * Raw XML node which represents an instruction operand.
     */
    @EqualsAndHashCode.Exclude
    private final XmlNode raw;

    /**
     * Constructor.
     * @param node Raw XML operand node.
     */
    public XmlOperand(final XmlNode node) {
        this.raw = node;
    }

    /**
     * Convert XML operand to an object.
     * @return Object.
     */
    @EqualsAndHashCode.Include
    public Object asObject() {
        final Object result;
        final String base = this.raw.attribute("base")
            .orElseThrow(
                () -> new IllegalStateException(
                    String.format(
                        "'%s' is not an argument because it doesn't have 'base' attribute",
                        this.raw
                    )
                )
            );
        if ("handle".equals(base)) {
            result = new XmlHandler(this.raw).asHandle();
        } else if ("annotation".equals(base)) {
            final XmlAnnotation xml = new XmlAnnotation(this.raw);
            result = new BytecodeAnnotation(xml.descriptor(), xml.visible(), xml.props());
        } else if ("annotation-property".equals(base)) {
            final XmlAnnotationProperty xml = new XmlAnnotationProperty(this.raw);
            result = xml.toBytecode();
        } else if ("tuple".equals(base)) {
            result = new XmlTuple(this.raw).asObject();
        } else {
            final boolean nullable = this.raw.attribute("scope").map("nullable"::equals)
                .orElse(false);
            if (nullable) {
                result = null;
            } else {
                final String text = this.raw.text();
                result = DataType.find(base).decode(text);
            }
        }
        return result;
    }
}
