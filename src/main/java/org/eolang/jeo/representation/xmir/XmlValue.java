/*
 * SPDX-FileCopyrightText: Copyright (c) 2016-2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.jeo.representation.xmir;

import java.util.regex.Pattern;
import org.eolang.jeo.representation.bytecode.BytecodeBytes;
import org.eolang.jeo.representation.bytecode.Codec;
import org.eolang.jeo.representation.bytecode.EoCodec;
import org.eolang.jeo.representation.bytecode.PlainLongCodec;
import org.eolang.jeo.representation.directives.EoFqn;

/**
 * XML value.
 * @since 0.6
 */
public final class XmlValue {

    /**
     * Hex radix.
     */
    private static final int RADIX = 16;

    /**
     * Boolean TRUE full qualified name.
     */
    private static final String TRUE = new EoFqn("true").fqn();

    /**
     * Boolean FALSE full qualified name.
     */
    private static final String FALSE = new EoFqn("false").fqn();

    /**
     * Number full qualified name.
     */
    private static final String NUMBER = new EoFqn("number").fqn();

    /**
     * Space pattern.
     */
    private static final Pattern DELIMITER = Pattern.compile("-");

    /**
     * XML node.
     */
    private final XmlNode node;

    /**
     * Constructor.
     * @param node XML node.
     */
    public XmlValue(final XmlNode node) {
        this.node = node;
    }

    /**
     * Convert hex string to human-readable string.
     * Example:
     * {@code
     *  "48 65 6C 6C 6F 20 57 6F 72 6C 64 21" -> "Hello World!"
     *  }
     * @return Human-readable string.
     */
    public String string() {
        return (String) this.object();
    }

    /**
     * Convert hex string to an object.
     * @return Object.
     */
    public Object object() {
        final String base = new XmlClosedObject(this.node).base();
        final Object res;
        if (XmlValue.isBoolean(base)) {
            res = base.equals(XmlValue.TRUE);
        } else {
            Codec codec = new EoCodec();
            if (!XmlValue.NUMBER.equals(base)) {
                codec = new PlainLongCodec(codec);
            }
            final int last = base.lastIndexOf('.');
            final String pure;
            if (last == -1) {
                pure = base;
            } else {
                pure = base.substring(last + 1);
            }
            res = new BytecodeBytes(pure, this.bytes()).object(codec);
        }
        return res;
    }

    /**
     * Object base.
     * @param base Object 'base' attribute value.
     * @return True if it's boolean, false otherwise.
     */
    private static boolean isBoolean(final String base) {
        return XmlValue.TRUE.equals(base) || XmlValue.FALSE.equals(base);
    }

    /**
     * Convert hex string to a byte array.
     * @return Byte array.
     */
    private byte[] bytes() {
        final String hex = this.hex();
        final byte[] res;
        if (hex.isEmpty()) {
            res = null;
        } else {
            final char[] chars = hex.toCharArray();
            final int length = chars.length;
            res = new byte[length / 2];
            for (int index = 0; index < length; index += 2) {
                res[index / 2] = (byte) Integer.parseInt(
                    String.copyValueOf(new char[]{chars[index], chars[index + 1]}), XmlValue.RADIX
                );
            }
        }
        return res;
    }

    /**
     * Hex string.
     * Example:
     * - "20 57 6F 72 6C 64 21" -> "20576F726C6421"
     * @return Hex string.
     */
    private String hex() {
        return XmlValue.DELIMITER.matcher(
            this.node.firstChild().text().trim()
        ).replaceAll("");
    }

}
