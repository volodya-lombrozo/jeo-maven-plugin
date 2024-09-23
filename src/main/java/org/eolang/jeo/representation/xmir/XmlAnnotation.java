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

import java.util.List;
import java.util.stream.Collectors;
import org.eolang.jeo.representation.bytecode.BytecodeAnnotation;
import org.eolang.jeo.representation.bytecode.BytecodeAnnotationValue;
import org.eolang.jeo.representation.directives.JeoFqn;

/**
 * Xmir representation of an annotation.
 * @since 0.1
 */
public class XmlAnnotation {

    /**
     * Xmir node.
     */
    private final XmlNode node;

    /**
     * Constructor.
     * @param xmlnode XML node.
     */
    XmlAnnotation(final XmlNode xmlnode) {
        this.node = xmlnode;
    }

    /**
     * Convert to bytecode.
     * @return Bytecode annotation.
     */
    public BytecodeAnnotation bytecode() {
        return new BytecodeAnnotation(
            this.descriptor(),
            this.visible(),
            this.props()
        );
    }

    /**
     * Annotation descriptor.
     * @return Descriptor.
     */
    private String descriptor() {
        return new XmlValue(this.child(0)).bytes().hex().decode();
//        return new HexString(
//            this.node.children().collect(Collectors.toList()).get(0).text()
//        ).decode();
    }

    /**
     * Annotation visible.
     * Is it runtime-visible?
     * @return True if visible at runtime, false otherwise.
     */
    private boolean visible() {
        return new XmlValue(this.child(1)).bytes().hex().decodeAsBoolean();
//        return new HexString(
//            this.node.children().collect(Collectors.toList()).get(1).text()
//        ).decodeAsBoolean();
    }

    private XmlNode child(final int index) {
        return this.node.children().collect(Collectors.toList()).get(index);
    }

    /**
     * Annotation properties.
     * @return Properties.
     */
    private List<BytecodeAnnotationValue> props() {
        return this.node.children()
            .filter(
                xmlnode -> xmlnode.hasAttribute("base", new JeoFqn("annotation-property").fqn())
            )
            .map(XmlAnnotationProperty::new)
            .map(XmlAnnotationProperty::bytecode)
            .collect(Collectors.toList());
    }
}
