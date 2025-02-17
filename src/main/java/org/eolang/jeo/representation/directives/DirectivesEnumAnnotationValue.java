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
package org.eolang.jeo.representation.directives;

import java.util.Iterator;
import java.util.Optional;
import org.xembly.Directive;

/**
 * An annotation value that is an enumeration.
 * @since 0.6
 */
public final class DirectivesEnumAnnotationValue implements Iterable<Directive> {

    /**
     * The name of the annotation property.
     */
    private final String name;

    /**
     * The descriptor of the enumeration.
     */
    private final String descriptor;

    /**
     * The actual enumeration value.
     */
    private final String value;

    /**
     * Constructor.
     * @param name The name of the annotation property.
     * @param descriptor The descriptor of the enumeration.
     * @param value The actual enumeration value.
     */
    public DirectivesEnumAnnotationValue(
        final String name, final String descriptor, final String value
    ) {
        this.name = name;
        this.descriptor = descriptor;
        this.value = value;
    }

    @Override
    public Iterator<Directive> iterator() {
        return new DirectivesJeoObject(
            "annotation-property",
            new DirectivesValue("ENUM"),
            new DirectivesValue(Optional.ofNullable(this.name).orElse("")),
            new DirectivesValue(this.descriptor),
            new DirectivesValue(this.value)
        ).iterator();
    }
}
