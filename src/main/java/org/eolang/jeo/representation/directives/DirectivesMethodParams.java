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
package org.eolang.jeo.representation.directives;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.objectweb.asm.Type;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Directives for method parameters.
 * @since 0.1
 */
public final class DirectivesMethodParams implements Iterable<Directive> {

    /**
     * Method descriptor.
     */
    private final String descriptor;

    /**
     * Parameter annotations.
     */
    private final Map<Integer, List<DirectivesAnnotation>> annotations;

    /**
     * Constructor.
     * @param descriptor Method descriptor.
     */
    public DirectivesMethodParams(final String descriptor) {
        this.descriptor = descriptor;
        this.annotations = new HashMap<>(0);
    }

    /**
     * Add a parameter annotation.
     * @param index Index of the parameter.
     * @param annotation Annotation.
     */
    public void annotation(final int index, final DirectivesAnnotation annotation) {
        this.annotations.compute(
            index,
            (key, initial) -> {
                final List<DirectivesAnnotation> res;
                if (Objects.isNull(initial)) {
                    res = new ArrayList<>(Collections.singletonList(annotation));
                } else {
                    initial.add(annotation);
                    res = initial;
                }
                return res;
            }
        );
    }

    @Override
    public Iterator<Directive> iterator() {
        final Directives directives = new Directives();
        final Type[] arguments = Type.getArgumentTypes(this.descriptor);
        for (int index = 0; index < arguments.length; ++index) {
            final Directives param = directives.add("o")
                .attr("base", "param")
                .attr("name", String.format("%s-%d", arguments[index], index));
            if (this.annotations.containsKey(index)) {
                this.annotations.get(index).forEach(param::append);
            }
            param.up();
        }
        return directives.iterator();
    }
}
