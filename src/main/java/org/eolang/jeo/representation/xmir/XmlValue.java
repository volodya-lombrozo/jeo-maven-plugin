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
        final String base = this.base();
        final Object res;
        if (XmlValue.isBoolean(base)) {
            res = new XmlClosedObject(this.node).optbase()
                .map(XmlValue.TRUE::equals)
                .orElse(false);
        } else {
            Codec codec = new EoCodec();
            final boolean nonumber = !new XmlClosedObject(this.node.child("o"))
                .optbase()
                .map(XmlValue.NUMBER::equals)
                .orElse(false);
            if (nonumber) {
                codec = new PlainLongCodec(codec);
            }
            final String result;
            final int last = base.lastIndexOf('.');
            if (last == -1) {
                result = base;
            } else {
                result = base.substring(last + 1);
            }
            res = new BytecodeBytes(result, this.bytes()).object(codec);
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
        if (hex.isEmpty() || hex.equals("--")) {
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
        // Refactor it!
        final XmlAbstractObject object = new XmlAbstractObject(this.node);
        if (object.optbase().isPresent()) {
            final String trim = object.children()
                .findFirst()
                .orElseThrow(
                    () -> new IllegalStateException(
                        String.format(
                            "Can't find child in '%s' to convert to hex",
                            this.node
                        )
                    )
                ).text()
                .trim();
            return XmlValue.DELIMITER.matcher(trim).replaceAll("");
        } else {
            return XmlValue.DELIMITER.matcher(
                this.node.firstChild().text().trim()
            ).replaceAll("");
        }
    }

    /**
     * Get the type of the object without a package.
     * @return Type without package.
     */
    private String base() {
        //Refactor it!
        final XmlAbstractObject ab = new XmlAbstractObject(this.node);
        if (ab.optbase().isPresent()) {
            return ab.base();
        }
        return new XmlClosedObject(this.node).base();
    }
}
