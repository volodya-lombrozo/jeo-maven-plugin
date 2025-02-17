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
import org.xembly.Directive;

/**
 * Xml directives for label entry in byrecode.
 * @since 0.1
 */
public final class DirectivesLabel implements Iterable<Directive> {

    /**
     * Bytecode label.
     */
    private final String label;

    /**
     * Label name.
     */
    private final String name;

    /**
     * Constructor.
     * @param label Bytecode label.
     */
    public DirectivesLabel(final String label) {
        this(label, "");
    }

    /**
     * Constructor.
     * @param label Bytecode label.
     * @param name Label name.
     */
    private DirectivesLabel(final String label, final String name) {
        this.label = label;
        this.name = name;
    }

    @Override
    public Iterator<Directive> iterator() {
        return new DirectivesValue(this.name, this.label).iterator();
    }
}
