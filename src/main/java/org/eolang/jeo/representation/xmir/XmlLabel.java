/*
 * SPDX-FileCopyrightText: Copyright (c) 2016-2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.jeo.representation.xmir;

import java.nio.charset.StandardCharsets;
import org.eolang.jeo.representation.bytecode.BytecodeBytes;
import org.eolang.jeo.representation.bytecode.BytecodeLabel;
import org.eolang.jeo.representation.bytecode.EoCodec;

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
//        final XmlNode first = this.node.children().findFirst()
//            .orElseThrow(IllegalStateException::new);
//        final XmlValue value = new XmlValue(
//            first
//        );
//        throw new IllegalStateException("Failed to convert label to bytecode, node: "
//            + this.node + "\n Value: " + first);
        byte[] res = (byte[]) new XmlValue(
            this.node.children().findFirst().orElseThrow(IllegalStateException::new)
        ).object();
        return  new BytecodeLabel(new String(res, StandardCharsets.UTF_8));

//        return (BytecodeLabel) new BytecodeBytes("label", res).object( new EoCodec());
//        return new BytecodeLabel(res);
//        return (BytecodeLabel) new XmlValue(this.node).object();


    }
}
