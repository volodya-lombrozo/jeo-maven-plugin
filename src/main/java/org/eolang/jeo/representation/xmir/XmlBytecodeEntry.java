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

import java.util.List;
import org.antlr.v4.runtime.Token;
import org.eolang.jeo.representation.bytecode.BytecodeMethod;
import org.w3c.dom.Node;

/**
 * XML representation of bytecode instruction or a label.
 * Usually each method in bytecode contains a list of bytecode entries.
 * Since they aren't always instructions, we call them entries.
 * @since 0.1
 */
public interface XmlBytecodeEntry {

    /**
     * Write instruction to the bytecode method.
     * @param method Bytecode Method where instruction should be written.
     */
    void writeTo(BytecodeMethod method);

    /**
     * Check if instruction has opcode.
     * @param opcode Opcode comparing with.
     * @return True if instruction has opcode.
     */
    boolean hasOpcode(int opcode);

    /**
     * Replace values of instruction arguments.
     * @param old Old value.
     * @param replacement Which value to set instead.
     */
    void replaceArguementsValues(String old, String replacement);

    List<Token> tokens();

    /**
     * Xml node.
     * @return Xml node.
     */
    Node node();
}
