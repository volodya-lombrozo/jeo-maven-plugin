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
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Translator that applies a translation to a batch of representations in parallel.
 * @since 0.2
 */
public final class ParallelTranslator implements Translator {

    /**
     * Original translation.
     */
    private final Function<? super Path, ? extends Path> translation;

    /**
     * Class loader.
     */
    private final ClassLoader loader;

    /**
     * Constructor.
     * @param translation Original translation.
     */
    ParallelTranslator(final Function<? super Path, ? extends Path> translation) {
        this.translation = translation;
        this.loader = Thread.currentThread().getContextClassLoader();
    }

    @Override
    public Stream<Path> apply(final Stream<Path> representations) {
        return representations.parallel().map(this::translate);
    }

    /**
     * Translate a representation.
     * This method is run in parallel.
     * Pay attention to the class loader;
     * It's set for each sub-thread to avoid class loading issues.
     * @param rep Representation to translate.
     * @return Translated representation.
     */
    private Path translate(final Path rep) {
        Thread.currentThread().setContextClassLoader(this.loader);
        return this.translation.apply(rep);
    }

}
