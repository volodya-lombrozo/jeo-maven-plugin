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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.eolang.jeo.representation.xmir.JcabiXmlDoc;

/**
 * EO objects.
 * Reads all EO objects from the folder.
 *
 * @since 0.1.0
 */
final class XmirFiles {

    /**
     * Where to read objects from.
     * Usually it's a folder with the name "generated-sources".
     * See <a href="https://maven.apache.org/guides/mini/guide-generating-sources.html">generated-sources</a>.
     */
    private final Path root;

    /**
     * Constructor.
     * @param xmirs Where to read objects from.
     */
    XmirFiles(final Path xmirs) {
        this.root = xmirs;
    }

    /**
     * All representations.
     * @return All representations.
     */
    public Stream<Path> all() {
        final Path path = this.root;
        try {
            return Files.walk(path).filter(Files::isRegularFile);
        } catch (final IOException exception) {
            throw new IllegalStateException(
                String.format("Can't read folder '%s'", path),
                exception
            );
        }
    }

    /**
     * Verify all the xmir files.
     */
    public void verify() {
        this.all().map(JcabiXmlDoc::new).forEach(JcabiXmlDoc::validate);
    }
}
