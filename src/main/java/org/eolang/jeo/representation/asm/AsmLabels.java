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
package org.eolang.jeo.representation.asm;

import java.util.HashMap;
import java.util.Map;
import org.eolang.jeo.representation.bytecode.BytecodeLabel;
import org.objectweb.asm.Label;

/**
 * Asm Method Labels.
 * Used during method generation to keep track of all the labels.
 * @since 0.6
 */
public final class AsmLabels {

    /**
     * All the method labels.
     */
    private final Map<String, Label> labels;

    /**
     * Constructor.
     */
    public AsmLabels() {
        this(new HashMap<>(0));
    }

    /**
     * Constructor.
     * @param labels All the labels.
     */
    public AsmLabels(final Map<String, Label> labels) {
        this.labels = labels;
    }

    /**
     * Get label by UID.
     * @param label Bytecode label.
     * @return Label.
     */
    public Label label(final BytecodeLabel label) {
        return this.labels.computeIfAbsent(label.uid(), id -> new Label());
    }
}
