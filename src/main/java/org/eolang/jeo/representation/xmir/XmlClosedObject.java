/*
 * SPDX-FileCopyrightText: Copyright (c) 2016-2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.jeo.representation.xmir;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Closed object in XMIR format.
 * @since 0.11.0
 */
public final class XmlClosedObject {

    private final XmlNode node;

    public XmlClosedObject(final XmlNode node) {
        this.node = node;
    }

    /**
     * Get the type of the object without a package.
     * @return Type without package.
     */
    String base() {
        return this.node.attribute("base").orElseThrow(() -> this.notFound("base"));
    }

    Optional<String> optbase() {
        return this.node.attribute("base");
    }

    Optional<XmlNode> child(final int index) {
        final List<XmlNode> children = this.node.children().collect(Collectors.toList());
        if (index < 0 || index >= children.size()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(children.get(index));
        }
    }


    private IllegalStateException notFound(final String name) {
        return new IllegalStateException(
            String.format(
                "The '%s' node doesn't have '%s' attribute, but it should have one",
                this.node, name
            )
        );
    }

}
