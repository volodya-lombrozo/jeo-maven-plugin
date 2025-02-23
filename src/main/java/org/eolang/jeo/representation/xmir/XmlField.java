/*
 * The MIT License (MIT)
 *
 * SPDX-FileCopyrightText: Copyright (c) 2016-2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.jeo.representation.xmir;

import java.util.Optional;
import java.util.stream.Collectors;
import lombok.ToString;
import org.eolang.jeo.representation.PrefixedName;
import org.eolang.jeo.representation.bytecode.BytecodeAnnotations;
import org.eolang.jeo.representation.bytecode.BytecodeField;

/**
 * XML field.
 * @since 0.1
 */
@ToString
public class XmlField {

    /**
     * Field node.
     */
    private final XmlNode node;

    /**
     * Constructor.
     * @param xmlnode Field node.
     */
    XmlField(final XmlNode xmlnode) {
        this.node = xmlnode;
    }

    /**
     * Convert to bytecode.
     * @return Bytecode field.
     */
    public BytecodeField bytecode() {
        return new BytecodeField(
            this.name(),
            this.descriptor(),
            this.signature(),
            this.value(),
            this.access(),
            this.annotations().map(XmlAnnotations::bytecode).orElse(new BytecodeAnnotations())
        );
    }

    /**
     * Field name.
     * @return Name.
     */
    private String name() {
        return new PrefixedName(
            this.node.attribute("as").orElseThrow(
                () -> new IllegalStateException(
                    String.format("Can't find field name in '%s'", this.node)
                )
            )
        ).decode();
    }

    /**
     * Field access modifiers.
     * @return Access modifiers.
     */
    private int access() {
        return this.find(Attribute.ACCESS).map(XmlValue::object).map(Integer.class::cast).orElse(0);
    }

    /**
     * Field descriptor.
     * @return Descriptor.
     */
    private String descriptor() {
        return this.find(Attribute.DESCRIPTOR).map(XmlValue::string)
            .filter(descriptor -> !descriptor.isEmpty())
            .orElse(null);
    }

    /**
     * Field signature.
     * @return Signature.
     */
    private String signature() {
        return this.find(Attribute.SIGNATURE).map(XmlValue::string)
            .filter(signature -> !signature.isEmpty())
            .orElse(null);
    }

    /**
     * Field value.
     * @return Value.
     */
    private Object value() {
        return new XmlOperand(
            this.node.children()
                .collect(Collectors.toList())
                .get(Attribute.VALUE.ordinal())
        ).asObject();
    }

    /**
     * Field annotations.
     * @return Annotations.
     */
    private Optional<XmlAnnotations> annotations() {
        return this.node.optchild("as", String.format("annotations-%s", this.name()))
            .map(XmlAnnotations::new);
    }

    /**
     * Find node text by attribute.
     * @param attribute Attribute.
     * @return Text.
     */
    private Optional<XmlValue> find(final Attribute attribute) {
        return Optional.of(
            new XmlValue(
                this.node.children().collect(Collectors.toList()).get(attribute.ordinal())
            )
        );
    }

    /**
     * Field attribute.
     * Pay attention that the order of the attributes is important.
     * They should be in the same order as in the XML representation.
     * @since 0.1
     */
    private enum Attribute {

        /**
         * Access modifier.
         * It's a number that represents sum of access modifiers.
         * For example, if the field is public and static, then the value will be 9.
         * See {@link org.objectweb.asm.Opcodes} for more details.
         */
        ACCESS,

        /**
         * Field descriptor.
         * For example, for field of type int the descriptor will be "I".
         */
        DESCRIPTOR,

        /**
         * Field signature.
         * The field signature is used for defining generic types.
         * Signature grammar:
         * <p>
         * {@code
         * TypeSignature: Z|C|B|S|I|F|J|D|FieldTypeSignature
         * FieldTypeSignature: ClassTypeSignature | [ TypeSignature | TypeVar
         * ClassTypeSignature: L Id ( / Id )* TypeArgs? ( . Id TypeArgs? )* ;
         * TypeArgs: < TypeArg+ >
         * TypeArg: * | ( + | - )? FieldTypeSignature
         * TypeVar: T Id ;
         * }
         * </p>
         * Examples:
         * <p>
         * {@code
         * List<E> -> Ljava/util/List<TE;>;
         * List<?> -> Ljava/util/List<*>;
         * List<? extends Number> -> Ljava/util/List<+Ljava/lang/Number;>;
         * List<? super Integer> -> Ljava/util/List<-Ljava/lang/Integer;>;
         * List<List<String>[]> -> Ljava/util/List<[Ljava/util/List<Ljava/lang/String;>;>;
         * HashMap<K, V>.HashIterator<K> -> Ljava/util/HashMap<TK;TV;>.HashIterator<TK;>;
         * }
         * </p>
         * You can read more in 4.1.1 section of the ASM
         * <a href="https://asm.ow2.io/asm4-guide.pdf">manual </a>
         */
        SIGNATURE,

        /**
         * Initial field value.
         * For example, for field of type int with value 19, the value will be "13".
         * Any data type will be represented as a hex string.
         * May not be set.
         */
        VALUE;
    }
}
