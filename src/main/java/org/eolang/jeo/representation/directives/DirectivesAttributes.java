/*
 * The MIT License (MIT)
 *
 * SPDX-FileCopyrightText: Copyright (c) 2016-2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.jeo.representation.directives;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.xembly.Directive;

/**
 * Directives for attributes.
 * @since 0.4
 */
public final class DirectivesAttributes implements Iterable<Directive> {

    /**
     * Name.
     */
    private final String name;

    /**
     * Attributes.
     */
    private final List<DirectivesAttribute> attributes;

    /**
     * Constructor.
     * @param attributes Separate attributes.
     */
    public DirectivesAttributes(final List<DirectivesAttribute> attributes) {
        this("attributes", attributes);
    }

    public DirectivesAttributes(final String name, final List<DirectivesAttribute> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    /**
     * Constructor.
     */
    DirectivesAttributes() {
        this(new ArrayList<>(0));
    }

    /**
     * Constructor.
     * @param attributes Attributes.
     */
    DirectivesAttributes(final DirectivesAttribute... attributes) {
        this(Arrays.asList(attributes));
    }

    @Override
    public Iterator<Directive> iterator() {
        return new DirectivesOptionalSeq(this.name, this.attributes).iterator();
    }
}
