/*
 * The MIT License (MIT)
 *
 * SPDX-FileCopyrightText: Copyright (c) 2016-2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.jeo.representation.xmir;

import java.util.Optional;
import org.eolang.jeo.representation.bytecode.BytecodeDefaultValue;
import org.eolang.jeo.representation.bytecode.BytecodeMethod;

/**
 * XMIR of annotation default value.
 * @since 0.3
 */
public final class XmlDefaultValue {

    /**
     * Default value XMIR node.
     */
    private final XmlNode node;

    /**
     * Constructor.
     * @param node Default value XMIR node.
     */
    XmlDefaultValue(final XmlNode node) {
        this.node = node;
    }

    /**
     * Convert to bytecode.
     * @return Bytecode default value.
     */
    public Optional<BytecodeDefaultValue> bytecode() {
        return this.node.children().findFirst().map(
            property -> new BytecodeDefaultValue(
                new XmlAnnotationValue(property).bytecode()
            )
        );
    }

    /**
     * Write to method.
     * @param method Method.
     */
    public void writeTo(final BytecodeMethod method) {
        this.bytecode().ifPresent(method::defvalue);
    }
}
