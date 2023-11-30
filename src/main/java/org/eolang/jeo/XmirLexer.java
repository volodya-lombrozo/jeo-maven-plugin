package org.eolang.jeo;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.eolang.parser.ProgramLexer;

class XmirLexer extends ProgramLexer {

    public XmirLexer(final String barMethod) {
        this(CharStreams.fromString(barMethod));
    }

    public XmirLexer(final CharStream input) {
        super(input);
    }

    @Override
    public Token nextToken() {
        return super.nextToken();
    }

}
