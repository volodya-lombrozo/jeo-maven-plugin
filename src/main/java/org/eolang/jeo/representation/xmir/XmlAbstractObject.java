/*
 * SPDX-FileCopyrightText: Copyright (c) 2016-2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.jeo.representation.xmir;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class XmlAbstractObject {

    /**
     * XML node of the closed object.
     */
    private final XmlNode node;

    /**
     * Constructor.
     * @param node XML node of the closed object.
     */
    XmlAbstractObject(final XmlNode node) {
        this.node = node;
    }

    /**
     * Get the type of the object.
     * @return Type.
     */
    String base() {
        return this.optbase().orElseThrow(() -> this.notFound("base"));
    }

    /**
     * Get the option type of the object.
     * @return Optional type.
     */
    Optional<String> optbase() {
        return this.node.children().findFirst()
            .filter(child -> child.hasAttribute("as", "base"))
            .map(
                child -> {
                    return new XmlValue(child).string();
                }
            );
    }

    /**
     * Create a new exception if the attribute is not found.
     * @param name Name of the attribute that is not found.
     * @return Exception indicating that the attribute is not found.
     */
    private IllegalStateException notFound(final String name) {
        return new IllegalStateException(
            String.format(
                "The '%s' node doesn't have '%s' attribute, but it should have one",
                this.node, name
            )
        );
    }

    Optional<String> attribute(final String name) {
        return this.node.attribute(name);
    }


    /**
     * Get the child node by index.
     * @param index Index of the child node.
     * @return Optional child node.
     */
    Optional<XmlNode> child(int index) {
        index++;
        final Optional<XmlNode> result;
        final List<XmlNode> children = this.node.children().collect(Collectors.toList());
        if (index < 0 || index >= children.size()) {
            result = Optional.empty();
        } else {
            result = Optional.ofNullable(children.get(index));
        }
        return result;
    }

    Stream<XmlNode> children() {
        final List<XmlNode> collect = this.node.children().collect(Collectors.toList());
        if (collect.isEmpty()) {
            throw new IllegalStateException(
                String.format(
                    "The '%s' node doesn't have any children, but it should have at least one",
                    this.node
                )
            );
        }
        return collect.subList(1, collect.size()).stream();
    }

    @Override
    public String toString() {
        return this.node.toString();
    }
}
