/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2025 Objectionary.com
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
package org.eolang.jeo;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.eolang.jeo.representation.PrefixedName;
import org.eolang.jeo.representation.XmirRepresentation;

/**
 * Assembling transformation.
 * @since 0.6
 */
public final class Assembling implements Transformation {

    /**
     * Target folder where to save the assembled class.
     */
    private final Path folder;

    /**
     * Representation to assemble.
     */
    private final Path from;

    /**
     * XMIR representation.
     */
    private final XmirRepresentation repr;

    /**
     * Constructor.
     * @param target Target folder.
     * @param representation Representation to assemble.
     */
    Assembling(final Path target, final Path representation) {
        this.folder = target;
        this.from = representation;
        this.repr = new XmirRepresentation(this.from);
    }

    @Override
    public Path source() {
        return this.from;
    }

    @Override
    public Path target() {
        final String name = new PrefixedName(this.repr.name()).decode();
        final String[] subpath = name.split("\\.");
        subpath[subpath.length - 1] = String.format("%s.class", subpath[subpath.length - 1]);
        return Paths.get(this.folder.toString(), subpath);
    }

    @Override
    public byte[] transform() {
        return this.repr.toBytecode().bytes();
    }
}
