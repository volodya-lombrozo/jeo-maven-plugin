/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2024 Objectionary.com
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eolang.jeo.representation.bytecode.BytecodeAttribute;
import org.eolang.jeo.representation.bytecode.BytecodeAttributes;
import org.eolang.jeo.representation.bytecode.BytecodeDefaultValue;
import org.eolang.jeo.representation.bytecode.BytecodeEntry;
import org.eolang.jeo.representation.bytecode.BytecodeFrame;
import org.eolang.jeo.representation.bytecode.BytecodeInstruction;
import org.eolang.jeo.representation.bytecode.BytecodeLabel;
import org.eolang.jeo.representation.bytecode.BytecodeLine;
import org.eolang.jeo.representation.bytecode.BytecodeMaxs;
import org.eolang.jeo.representation.bytecode.BytecodeMethod;
import org.eolang.jeo.representation.bytecode.BytecodeMethodParameter;
import org.eolang.jeo.representation.bytecode.BytecodeMethodParameters;
import org.eolang.jeo.representation.bytecode.BytecodeMethodProperties;
import org.eolang.jeo.representation.bytecode.BytecodeTryCatchBlock;
import org.eolang.jeo.representation.bytecode.LocalVariable;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * Asm method.
 * Asm parser for a method.
 * @since 0.6
 */
final class AsmMethod {

    /**
     * Method node.
     */
    private final MethodNode node;

    /**
     * Constructor.
     * @param node Method node.
     */
    AsmMethod(final MethodNode node) {
        this.node = node;
    }

    /**
     * Convert asm method to domain method.
     * @return Domain method.
     */
    BytecodeMethod bytecode() {
        return new BytecodeMethod(
            AsmMethod.tryblocks(this.node),
            AsmMethod.instructions(this.node),
            new AsmAnnotations(this.node).annotations(),
            new BytecodeMethodProperties(
                this.node.access,
                this.node.name,
                this.node.desc,
                this.node.signature,
                AsmMethod.parameters(this.node),
                this.node.exceptions.toArray(new String[0])
            ),
            AsmMethod.defvalues(this.node),
            AsmMethod.maxs(this.node),
            AsmMethod.attributes(this.node)
        );
    }

    /**
     * Convert asm method to domain method attributes.
     * @param node Asm method node.
     * @return Domain method attributes.
     */
    private static BytecodeAttributes attributes(final MethodNode node) {
        final List<LocalVariableNode> variables = node.localVariables;
        final BytecodeAttributes result;
        if (variables == null) {
            result = new BytecodeAttributes();
        } else {
            result = new BytecodeAttributes(
                variables.stream().map(LocalVariable::new).toArray(BytecodeAttribute[]::new)
            );
        }
        return result;
    }

    /**
     * Convert asm method to domain method parameters.
     * @param node Asm method node.
     * @return Domain method parameters.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static BytecodeMethodParameters parameters(final MethodNode node) {
        final Type[] types = Type.getArgumentTypes(node.desc);
        final List<BytecodeMethodParameter> params = new ArrayList<>(types.length);
        for (int index = 0; index < types.length; ++index) {
            params.add(
                new BytecodeMethodParameter(
                    index,
                    AsmMethod.paramName(node, index),
                    AsmMethod.paramAccess(node, index),
                    types[index],
                    new AsmAnnotations(
                        AsmMethod.paramAnnotations(node.visibleParameterAnnotations, index),
                        AsmMethod.paramAnnotations(node.invisibleParameterAnnotations, index)
                    ).annotations()
                )
            );
        }
        return new BytecodeMethodParameters(params);
    }

    static List<AnnotationNode> paramAnnotations(
        final List<AnnotationNode>[] all, final int index
    ) {
        if (Objects.isNull(all)) {
            return new ArrayList<>(0);
        }
        return all.length > index ? all[index] : new ArrayList<>(0);
    }

    /**
     * Retrieve method parameter access from asm method.
     * @param node Asm method node.
     * @param index Parameter index.
     * @return Parameter access.
     */
    private static int paramAccess(final MethodNode node, final int index) {
        final int result;
        if (node.parameters != null && node.parameters.size() > index) {
            result = node.parameters.get(index).access;
        } else {
            result = 0;
        }
        return result;
    }

    /**
     * Retrieve method parameter name from asm method.
     * @param node Asm method node.
     * @param index Parameter index.
     * @return Parameter name.
     */
    private static String paramName(final MethodNode node, final int index) {
        final String result;
        if (node.parameters != null && node.parameters.size() > index) {
            result = node.parameters.get(index).name;
        } else {
            result = String.format("arg%d", index);
        }
        return result;
    }

    /**
     * Convert asm method to domain method maxs.
     * @param node Asm method node.
     * @return Domain method maxs.
     */
    private static BytecodeMaxs maxs(final MethodNode node) {
        return new BytecodeMaxs(node.maxStack, node.maxLocals);
    }

    /**
     * Convert asm method to domain method tryblocks.
     * @param node Asm method node.
     * @return Domain method tryblocks.
     */
    private static List<BytecodeEntry> tryblocks(final MethodNode node) {
        return node.tryCatchBlocks.stream().map(AsmMethod::tryblock).collect(Collectors.toList());
    }

    /**
     * Convert asm try-catch block to domain try-catch block.
     * @param node Asm try-catch block node.
     * @return Domain try-catch block.
     */
    private static BytecodeEntry tryblock(final TryCatchBlockNode node) {
        return new BytecodeTryCatchBlock(
            node.start.getLabel(),
            node.end.getLabel(),
            node.handler.getLabel(),
            node.type
        );
    }

    /**
     * Convert asm class to domain class.
     * @param node Asm class node.
     * @return Domain class.
     */
    private static List<BytecodeEntry> instructions(final MethodNode node) {
        return Arrays.stream(node.instructions.toArray())
            .map(AsmMethod::instruction)
            .collect(Collectors.toList());
    }

    /**
     * Convert asm instruction to domain instruction.
     * @param node Asm instruction node.
     * @return Domain instruction.
     */
    private static BytecodeEntry instruction(final AbstractInsnNode node) {
        final BytecodeEntry result;
        switch (node.getType()) {
            case AbstractInsnNode.INSN:
                result = new BytecodeInstruction(node.getOpcode());
                break;
            case AbstractInsnNode.INT_INSN:
                final IntInsnNode instr = IntInsnNode.class.cast(node);
                result = new BytecodeInstruction(
                    instr.getOpcode(),
                    instr.operand
                );
                break;
            case AbstractInsnNode.VAR_INSN:
                final VarInsnNode varinstr = VarInsnNode.class.cast(node);
                result = new BytecodeInstruction(
                    varinstr.getOpcode(), varinstr.var
                );
                break;
            case AbstractInsnNode.TYPE_INSN:
                final TypeInsnNode typeinstr = TypeInsnNode.class.cast(node);
                result = new BytecodeInstruction(
                    typeinstr.getOpcode(),
                    typeinstr.desc
                );
                break;
            case AbstractInsnNode.FIELD_INSN:
                final FieldInsnNode field = FieldInsnNode.class.cast(node);
                result = new BytecodeInstruction(
                    field.getOpcode(),
                    field.owner,
                    field.name,
                    field.desc
                );
                break;
            case AbstractInsnNode.METHOD_INSN:
                final MethodInsnNode method = MethodInsnNode.class.cast(node);
                result = new BytecodeInstruction(
                    method.getOpcode(),
                    method.owner,
                    method.name,
                    method.desc,
                    method.itf
                );
                break;
            case AbstractInsnNode.INVOKE_DYNAMIC_INSN:
                final InvokeDynamicInsnNode dynamic = InvokeDynamicInsnNode.class.cast(node);
                result = new BytecodeInstruction(
                    dynamic.getOpcode(),
                    Stream.concat(
                        Stream.of(
                            dynamic.name,
                            dynamic.desc,
                            dynamic.bsm
                        ),
                        Arrays.stream(dynamic.bsmArgs)
                    ).toArray(Object[]::new)
                );
                break;
            case AbstractInsnNode.JUMP_INSN:
                final JumpInsnNode jump = JumpInsnNode.class.cast(node);
                result = new BytecodeInstruction(
                    jump.getOpcode(),
                    jump.label.getLabel()
                );
                break;
            case AbstractInsnNode.LABEL:
                final LabelNode label = LabelNode.class.cast(node);
                result = new BytecodeLabel(
                    label.getLabel()
                );
                break;
            case AbstractInsnNode.LDC_INSN:
                final LdcInsnNode ldc = LdcInsnNode.class.cast(node);
                result = new BytecodeInstruction(
                    ldc.getOpcode(),
                    ldc.cst
                );
                break;
            case AbstractInsnNode.IINC_INSN:
                final IincInsnNode iinc = IincInsnNode.class.cast(node);
                result = new BytecodeInstruction(
                    iinc.getOpcode(),
                    iinc.var,
                    iinc.incr
                );
                break;
            case AbstractInsnNode.TABLESWITCH_INSN:
                final TableSwitchInsnNode table = TableSwitchInsnNode.class.cast(node);
                result = new BytecodeInstruction(
                    table.getOpcode(),
                    Stream.concat(
                        Stream.of(table.min, table.max, table.dflt.getLabel()),
                        table.labels.stream().map(LabelNode::getLabel)
                    ).toArray(Object[]::new)
                );
                break;
            case AbstractInsnNode.LOOKUPSWITCH_INSN:
                final LookupSwitchInsnNode lookup = LookupSwitchInsnNode.class.cast(node);
                result = new BytecodeInstruction(
                    lookup.getOpcode(),
                    Stream.concat(
                        Stream.of(lookup.dflt.getLabel()),
                        Stream.concat(
                            lookup.keys.stream(),
                            lookup.labels.stream().map(LabelNode::getLabel)
                        )
                    ).toArray(Object[]::new)
                );
                break;
            case AbstractInsnNode.MULTIANEWARRAY_INSN:
                final MultiANewArrayInsnNode multiarr = MultiANewArrayInsnNode.class.cast(node);
                result = new BytecodeInstruction(
                    multiarr.getOpcode(),
                    multiarr.desc,
                    multiarr.dims
                );
                break;
            case AbstractInsnNode.FRAME:
                final FrameNode frame = FrameNode.class.cast(node);
                result = new BytecodeFrame(frame.type, frame.local, frame.stack);
                break;
            case AbstractInsnNode.LINE:
                result = new BytecodeLine();
                break;
            default:
                throw new IllegalStateException(
                    String.format("Unknown instruction type: %s", node)
                );
        }
        return result;
    }

    /**
     * Convert asm default value to domain default value.
     * @param node Asm method node.
     * @return Domain default value.
     */
    private static List<BytecodeDefaultValue> defvalues(final MethodNode node) {
        final List<BytecodeDefaultValue> result;
        if (node.annotationDefault == null) {
            result = Collections.emptyList();
        } else {
            result = Collections.singletonList(
                new BytecodeDefaultValue(
                    new AsmAnnotationProperty(node.annotationDefault).bytecode()
                )
            );
        }
        return result;
    }
}
