/*
 * The MIT License (MIT)
 *
 * SPDX-FileCopyrightText: Copyright (c) 2016-2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.jeo.representation.directives;

import java.util.Iterator;
import org.eolang.jeo.representation.bytecode.BytecodeLabel;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.xembly.Directive;

/**
 * Operand XML directives.
 * @since 0.1
 */
public final class DirectivesOperand implements Iterable<Directive> {

    /**
     * Raw operand.
     */
    private final Object raw;

    /**
     * Constructor.
     * @param operand Raw operand.
     */
    DirectivesOperand(final Object operand) {
        this.raw = operand;
    }

    @Override
    public Iterator<Directive> iterator() {
        final Iterator<Directive> result;
        if (this.raw instanceof Label) {
            result = new BytecodeLabel(this.raw.toString()).directives().iterator();
        } else if (this.raw instanceof Handle) {
            result = new DirectivesHandle((Handle) this.raw).iterator();
        } else {
            result = new DirectivesValue(this.raw).iterator();
        }
        return result;
    }

}
