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
package org.eolang.jeo.representation.directives;

import com.jcabi.matchers.XhtmlMatchers;
import java.util.stream.Stream;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.xembly.Directives;
import org.xembly.ImpossibleModificationException;
import org.xembly.Xembler;

/**
 * Directives sequence test cases.
 * Test cases for {@link DirectivesSeq}.
 * @since 0.6
 */
final class DirectivesSeqTest {

    @Test
    void convertsToNumberedSeq() throws ImpossibleModificationException {
        MatcherAssert.assertThat(
            "We expect to get a numbered sequence with two elements",
            new Xembler(
                new DirectivesSeq(
                    new DirectivesValue("1"), new DirectivesValue("2")
                )
            ).xml(),
            XhtmlMatchers.hasXPaths(
                "/o[contains(@base,'seq.of2') and contains(@as,'seq')]",
                "/o[contains(@base,'seq.of2') and contains(@as,'seq')]/o[contains(@base,'string')]/o[contains(@base, 'org.eolang.bytes') and text()='31-']",
                "/o[contains(@base,'seq.of2') and contains(@as,'seq')]/o[contains(@base,'string')]/o[contains(@base, 'org.eolang.bytes') and text()='32-']"
            )
        );
    }

    @ParameterizedTest
    @MethodSource("sequences")
    void computesSize(
        final DirectivesSeq actual, final int expected
    ) throws ImpossibleModificationException {
        MatcherAssert.assertThat(
            "The size of the sequence is not as expected",
            new Xembler(actual).xml(),
            XhtmlMatchers.hasXPath(
                String.format(
                    "/o[contains(@base,'seq.of%d') and contains(@as,'seq')]",
                    expected
                )
            )
        );
    }

    /**
     * Sequences to test.
     * @return Stream of arguments.
     */
    static Stream<Arguments> sequences() {
        return Stream.of(
            Arguments.of(
                new DirectivesSeq(
                    new DirectivesValue("1"), new DirectivesValue("2")
                ),
                2
            ),
            Arguments.of(new DirectivesSeq(), 0),
            Arguments.of(new DirectivesSeq(new Directives(), new Directives()), 0),
            Arguments.of(
                new DirectivesSeq(
                    new DirectivesValue("1"), new Directives(),
                    new Directives(), new DirectivesValue("4")
                ),
                2
            ),
            Arguments.of(
                new DirectivesSeq(), 0
            )
        );
    }
}
