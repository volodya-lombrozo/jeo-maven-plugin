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
import java.util.Iterator;
import java.util.List;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Directives Method.
 * @since 0.1
 */
public final class DirectivesMethod implements Iterable<Directive> {

    /**
     * Method name.
     */
    private final String name;

    /**
     * Method properties.
     */
    private final DirectivesMethodProperties properties;

    /**
     * Method instructions.
     */
    private final List<Iterable<Directive>> instructions;

    /**
     * Method exceptions.
     */
    private final List<Iterable<Directive>> exceptions;

    /**
     * Opcodes counting.
     */
    private final boolean counting;

    /**
     * Constructor.
     * @param name Method name
     */
    public DirectivesMethod(final String name) {
        this(name, new DirectivesMethodProperties());
    }

    /**
     * Constructor.
     * @param name Method name
     * @param properties Method properties
     */
    public DirectivesMethod(final String name, final DirectivesMethodProperties properties) {
        this(name, true, properties);
    }

    /**
     * Constructor.
     * @param name Method name
     * @param counting Opcodes counting
     * @param properties Method properties
     */
    public DirectivesMethod(
        final String name,
        final boolean counting,
        final DirectivesMethodProperties properties
    ) {
        this.name = name;
        this.properties = properties;
        this.counting = counting;
        this.instructions = new ArrayList<>(0);
        this.exceptions = new ArrayList<>(0);
    }

    /**
     * Add opcode to the directives.
     * @param opcode Opcode
     * @param operands Operands
     * @return This object
     */
    public DirectivesMethod opcode(final int opcode, final Object... operands) {
        this.instructions.add(new DirectivesInstruction(opcode, this.counting, operands));
        return this;
    }

    /**
     * Add operand to the directives.
     * @param directives Operand directives.
     */
    public void operand(final Iterable<Directive> directives) {
        this.instructions.add(directives);
    }

    @Override
    public Iterator<Directive> iterator() {
        final Directives directives = new Directives();
        final String eoname;
        if (this.name.equals("<init>")) {
            eoname = "new";
        } else {
            eoname = this.name;
        }
        directives.add("o")
            .attr("abstract", "")
            .attr("name", eoname)
            .append(this.properties)
            .add("o")
            .attr("base", "seq")
            .attr("name", "@")
            .add("o")
            .attr("base", "tuple")
            .attr("star", "");
        this.instructions.forEach(directives::append);
        directives.up();
        directives.up();
        if (!this.exceptions.isEmpty()) {
            directives.add("o")
                .attr("base", "tuple")
                .attr("star", "")
                .attr("name", "trycatchblocks");
            this.exceptions.forEach(directives::append);
            directives.up();
        }
        directives.up();
        return directives.iterator();
    }

    /**
     * Add exception to the directives.
     * @param exception Exception directives.
     */
    void exception(final Iterable<Directive> exception) {
        this.exceptions.add(exception);
    }
}
