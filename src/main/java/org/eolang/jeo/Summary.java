/*
 * SPDX-FileCopyrightText: Copyright (c) 2016-2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.jeo;

import com.jcabi.log.Logger;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Translation summary log.
 * @since 0.2
 */
public final class Summary implements Translator {

    /**
     * Process name.
     */
    private final String process;

    /**
     * Past participle of the process.
     * Usually it is something like:
     * "disassembled", "assembled", etc.
     */
    private final String participle;

    /**
     * From where.
     */
    private final Path input;

    /**
     * To where.
     */
    private final Path output;

    /**
     * Original translator.
     */
    private final Translator original;

    /**
     * Constructor.
     * @param process Process name.
     * @param participle Past participle of the process.
     * @param input From where.
     * @param output To where.
     * @param original Original translator.
     * @checkstyle ParameterNumberCheck (5 lines)
     */
    Summary(
        final String process,
        final String participle,
        final Path input,
        final Path output,
        final Translator original
    ) {
        this.process = process;
        this.participle = participle;
        this.input = input;
        this.output = output;
        this.original = original;
    }

    @Override
    public Stream<Path> apply(final Stream<Path> representations) {
        Logger.info(
            this,
            "%s files from '%[file]s' to '%[file]s'",
            this.process,
            this.input,
            this.output
        );
        final long start = System.currentTimeMillis();
        final AtomicInteger counter = new AtomicInteger();
        return this.original.apply(representations)
            .peek(rep -> counter.incrementAndGet())
            .onClose(
                () -> Logger.info(
                    this,
                    "Total %d files were %s in %[ms]s",
                    counter.get(),
                    this.participle,
                    System.currentTimeMillis() - start
                )
            );
    }
}
