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

import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

/**
 * Bytecode annotation.
 * @since 0.2
 */
public final class BytecodeAnnotation {

    /**
     * Descriptor.
     */
    private final String descriptor;

    /**
     * Visible.
     */
    private final boolean visible;

    /**
     * Properties.
     */
    private final List<BytecodeAnnotationProperty> properties;

    /**
     * Constructor.
     * @param descriptor Descriptor.
     * @param visible Visible.
     */
    public BytecodeAnnotation(final String descriptor, final boolean visible) {
        this(descriptor, visible, new ArrayList<>(0));
    }

    /**
     * Constructor.
     * @param descriptor Descriptor.
     * @param visible Visible.
     * @param properties Properties.
     */
    public BytecodeAnnotation(
        final String descriptor,
        final boolean visible,
        final List<BytecodeAnnotationProperty> properties
    ) {
        this.descriptor = descriptor;
        this.visible = visible;
        this.properties = properties;
    }

    /**
     * Write class annotation.
     * @param visitor Visitor.
     * @return This.
     */
    public BytecodeAnnotation write(final ClassVisitor visitor) {
        final AnnotationVisitor avisitor = visitor.visitAnnotation(this.descriptor, this.visible);
        this.properties.forEach(property -> property.write(avisitor));
        return this;
    }

    /**
     * Write field annotation.
     * @param visitor Visitor.
     * @return This.
     */
    public BytecodeAnnotation write(final FieldVisitor visitor) {
        visitor.visitAnnotation(this.descriptor, this.visible);
        return this;
    }
}
