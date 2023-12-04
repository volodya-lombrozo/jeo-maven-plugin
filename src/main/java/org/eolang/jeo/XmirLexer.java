package org.eolang.jeo;

import java.util.Deque;
import java.util.LinkedList;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Token;
import org.eolang.jeo.representation.xmir.XmlBytecodeEntry;
import org.eolang.jeo.representation.xmir.XmlMethod;
import org.eolang.parser.ProgramLexer;

class XmirLexer extends ProgramLexer {

    private final Deque<Token> tokens;

    public XmirLexer(final String barMethod) {
        this(CharStreams.fromString(barMethod));
    }

    public XmirLexer(final CharStream input) {
        super(input);
        this.tokens = XmirLexer.parse(input.toString());
    }

    public XmirLexer(final XmlMethod method) {
        super(CharStreams.fromString(method.toString()));
        this.tokens = XmirLexer.parse(method);
    }

    @Override
    public Token nextToken() {
        if (this.tokens.isEmpty()) {
            return new CommonToken(DecompilerParser.EOF);
        }
        return this.tokens.pop();
    }

    private static Deque<Token> parse(final XmlMethod method) {
        Deque<Token> res = new LinkedList<>();
        for (final XmlBytecodeEntry instruction : method.instructions()) {
            res.addAll(instruction.tokens());
        }
        return res;
    }

    private static Deque<Token> parse(final String xml) {
        return parse(new XmlMethod(xml));
    }

}
