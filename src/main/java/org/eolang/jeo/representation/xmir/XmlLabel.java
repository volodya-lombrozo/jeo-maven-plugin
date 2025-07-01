/*
 * SPDX-FileCopyrightText: Copyright (c) 2016-2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.jeo.representation.xmir;

import java.nio.charset.StandardCharsets;
import org.eolang.jeo.representation.bytecode.BytecodeLabel;

/**
 * XML representation of bytecode label.
 * @since 0.1
 */
public final class XmlLabel implements XmlBytecodeEntry {

    /**
     * Label node.
     */
    private final XmlAbstractObject node;

    /**
     * Constructor.
     * @param node Label node.
     */
    XmlLabel(final XmlNode node) {
        this.node = new XmlAbstractObject(node);
    }

    /**
     * Converts label to bytecode.
     * @return Bytecode label.
     */
    public BytecodeLabel bytecode() {
        final XmlNode xmlnode = this.node.children()
            .findFirst()
            .orElseThrow(IllegalStateException::new);
        return new BytecodeLabel(
            new String((byte[]) new XmlValue(xmlnode).object(), StandardCharsets.UTF_8)
        );
    }
}
