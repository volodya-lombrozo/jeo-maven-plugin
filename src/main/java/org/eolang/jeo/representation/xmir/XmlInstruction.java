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
package org.eolang.jeo.representation.xmir;

import com.jcabi.xml.XMLDocument;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Token;
import org.eolang.jeo.DecompilerParser;
import org.eolang.jeo.representation.HexData;
import org.eolang.jeo.representation.bytecode.BytecodeMethod;
import org.eolang.jeo.representation.directives.DirectivesInstruction;
import org.objectweb.asm.Opcodes;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xembly.Transformers;
import org.xembly.Xembler;

/**
 * Bytecode instruction from XML.
 * @since 0.1
 * @todo #157:90min Hide internal node representation in XmlInstruction.
 *  This class should not expose internal node representation.
 *  We have to consider to add methods or classes in order to avoid
 *  exposing internal node representation.
 */
@SuppressWarnings("PMD.TooManyMethods")
public final class XmlInstruction implements XmlBytecodeEntry {

    /**
     * Instruction node.
     */
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private final Node node;

    /**
     * All found labels.
     */
    private final AllLabels labels;

    /**
     * Constructor.
     * @param xml XML node as String.
     */
    public XmlInstruction(final String xml) {
        this(new XMLDocument(xml).node().getFirstChild());
    }

    /**
     * Constructor.
     * @param opcode Opcode.
     * @param args Arguments.
     */
    public XmlInstruction(final int opcode, final Object... args) {
        this(
            new Xembler(
                new DirectivesInstruction(opcode, args),
                new Transformers.Node()
            ).xmlQuietly()
        );
    }

    /**
     * Constructor.
     * @param node Instruction node.
     */
    XmlInstruction(final Node node) {
        this.node = node;
        this.labels = new AllLabels();
    }

    @Override
    public void writeTo(final BytecodeMethod method) {
        method.instruction(this.code(), this.arguments());
    }

    @Override
    public boolean hasOpcode(final int opcode) {
        return this.code() == opcode;
    }

    @Override
    public void replaceArguementsValues(final String old, final String replacement) {
        final String oldname = new HexData(old).value();
        new XmlNode(this.node).children().forEach(
            child -> {
                final String content = child.text();
                if (oldname.equals(content)) {
                    child.withText(new HexData(replacement).value());
                }
            }
        );
    }

    //TODO: HERE THE TOKENS ARE GENERATED.
    @Override
    public List<Token> tokens() {
        final List<Token> res = new ArrayList<>(0);
        final Object[] arguments = arguments();
        switch (this.code()) {
            case Opcodes.DLOAD:
                res.add(new CommonToken(DecompilerParser.DLOAD));
                res.add(new CommonToken(DecompilerParser.IVALUE, String.valueOf(arguments[0])));
                break;
            case Opcodes.DCONST_0:
                res.add(new CommonToken(DecompilerParser.DCONST_0));
                break;
            case Opcodes.DCMPL:
                res.add(new CommonToken(DecompilerParser.DCMPL));
                break;
            case Opcodes.IFLE:
                res.add(new CommonToken(DecompilerParser.IFLE));
                res.add(new CommonToken(DecompilerParser.LABEL, String.valueOf(arguments[0])));
                break;
            case Opcodes.ICONST_5:
                res.add(new CommonToken(DecompilerParser.ICONST_5));
                break;
            case Opcodes.IRETURN:
                res.add(new CommonToken(DecompilerParser.IRETURN));
                break;
            case Opcodes.RETURN:
                res.add(new CommonToken(DecompilerParser.RETURN));
                break;
            case Opcodes.BIPUSH:
                res.add(new CommonToken(DecompilerParser.BIPUSH));
                res.add(new CommonToken(DecompilerParser.IVALUE, String.valueOf(arguments[0])));
                break;
            case Opcodes.INVOKEVIRTUAL:
                res.add(new CommonToken(DecompilerParser.INVOKEVIRTUAL));
                res.add(new CommonToken(DecompilerParser.SVALUE, String.valueOf(arguments[0])));
                res.add(new CommonToken(DecompilerParser.SVALUE, String.valueOf(arguments[1])));
                res.add(new CommonToken(DecompilerParser.SVALUE, String.valueOf(arguments[2])));
                break;
            case Opcodes.LDC:
                res.add(new CommonToken(DecompilerParser.LDC));
                res.add(new CommonToken(DecompilerParser.SVALUE, String.valueOf(arguments[0])));
                break;
            case Opcodes.GETSTATIC:
                res.add(new CommonToken(DecompilerParser.GETSTATIC));
                res.add(new CommonToken(DecompilerParser.SVALUE, String.valueOf(arguments[0])));
                res.add(new CommonToken(DecompilerParser.SVALUE, String.valueOf(arguments[1])));
                res.add(new CommonToken(DecompilerParser.SVALUE, String.valueOf(arguments[2])));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this.code());
        }
        return res;
    }

    /**
     * Instruction arguments.
     * @return Arguments.
     */
    public Object[] arguments() {
        return new XmlNode(this.node)
            .children()
            .skip(1)
            .map(this::argument)
            .toArray();
    }

    @Override
    public boolean equals(final Object other) {
        final boolean result;
        if (this == other) {
            result = true;
        } else if (other == null || this.getClass() != other.getClass()) {
            result = false;
        } else {
            result = this.equals(this.node, ((XmlInstruction) other).node);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.node);
    }

    @Override
    public String toString() {
        return new XMLDocument(this.node).toString();
    }

    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    @Override
    public Node node() {
        return this.node;
    }

    /**
     * Extract argument value from XmlNode.
     * @param argument XmlNode with argument value.
     * @return Argument value.
     */
    private Object argument(final XmlNode argument) {
        final String attr = argument.attribute("base")
            .orElseThrow(
                () -> new IllegalStateException(
                    String.format(
                        "'%s' is not an argument because it doesn't have 'base' attribute",
                        argument
                    )
                )
            );
        final Object result;
        if (attr.equals("int")) {
            result = new HexString(argument.text()).decodeAsInt();
        } else if (attr.equals("label")) {
            result = this.labels.label(argument.text());
        } else {
            result = new HexString(argument.text()).decode();
        }
        return result;
    }

    /**
     * Instruction code.
     * @return Code.
     */
    private int code() {
        return new HexString(new XmlNode(this.node).firstChild().text()).decodeAsInt();
    }

    /**
     * Check if two nodes are equal.
     * @param first First node.
     * @param second Second node.
     * @return True if nodes are equal.
     */
    private boolean equals(final Node first, final Node second) {
        final boolean result;
        if (first.getNodeType() == second.getNodeType()) {
            if (first.getNodeType() == Node.TEXT_NODE) {
                result = first.getTextContent().trim().equals(second.getTextContent().trim());
            } else {
                result = this.areElementsEqual(first, second);
            }
        } else {
            result = false;
        }
        return result;
    }

    /**
     * Check if two elements are equal.
     * @param first First element.
     * @param second Second element.
     * @return True if elements are equal.
     */
    private boolean areElementsEqual(final Node first, final Node second) {
        return first.getNodeName().equals(second.getNodeName())
            && XmlInstruction.hasSameAttributes(first, second)
            && this.hasSameChildren(first, second);
    }

    /**
     * Check if two nodes have the same attributes.
     * @param first First node.
     * @param second Second node.
     * @return True if nodes have the same attributes.
     */
    private static boolean hasSameAttributes(final Node first, final Node second) {
        boolean result = true;
        final NamedNodeMap attributes = first.getAttributes();
        final int length = attributes.getLength();
        if (length == second.getAttributes().getLength()) {
            for (int index = 0; index < length; ++index) {
                final Node left = attributes.item(index);
                final Node right = second.getAttributes().getNamedItem(left.getNodeName());
                if (!XmlInstruction.areAttributesEqual(left, right)) {
                    result = false;
                    break;
                }
            }
        } else {
            result = false;
        }
        return result;
    }

    /**
     * Check if two attributes are equal.
     * @param first First attribute.
     * @param second Second attribute.
     * @return True if attributes are equal.
     */
    private static boolean areAttributesEqual(final Node first, final Node second) {
        final boolean result;
        if (first != null && second != null && first.getNodeName().equals(second.getNodeName())) {
            result = first.getNodeValue().equals(second.getNodeValue());
        } else {
            result = false;
        }
        return result;
    }

    /**
     * Check if two nodes have the same children.
     * @param left Left node.
     * @param right Right node.
     * @return True if nodes have the same children.
     */
    private boolean hasSameChildren(final Node left, final Node right) {
        final List<Node> first = XmlInstruction.children(left);
        final List<Node> second = XmlInstruction.children(right);
        return first.size() == second.size()
            && IntStream.range(0, first.size())
            .allMatch(index -> this.equals(first.get(index), second.get(index)));
    }

    /**
     * Get children nodes.
     * @param root Root node.
     * @return Children nodes.
     */
    private static List<Node> children(final Node root) {
        final NodeList all = root.getChildNodes();
        final List<Node> res = new ArrayList<>(0);
        for (int index = 0; index < all.getLength(); ++index) {
            final Node item = all.item(index);
            final int type = item.getNodeType();
            if (type == Node.ELEMENT_NODE) {
                res.add(item);
            }
            if (type == Node.TEXT_NODE && !item.getTextContent().isBlank()) {
                res.add(item);
            }
        }
        return res;
    }
}
