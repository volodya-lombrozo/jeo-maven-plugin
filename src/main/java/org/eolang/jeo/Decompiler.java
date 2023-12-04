package org.eolang.jeo;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eolang.jeo.representation.xmir.XmlMethod;

public class Decompiler {
    public String decompile(final String barMethod) {
        final XmirLexer lexer = new XmirLexer(barMethod);
        final DecompilerParser parser = new DecompilerParser(
            new CommonTokenStream(lexer)
        );
        final EoDecompilerListener listener = new EoDecompilerListener();
        new ParseTreeWalker().walk(listener, parser.program());
        return listener.result();
    }

    public String decompile(XmlMethod barMethod) {
        final XmirLexer lexer = new XmirLexer(barMethod);
        final DecompilerParser parser = new DecompilerParser(
            new CommonTokenStream(lexer)
        );
        final EoDecompilerListener listener = new EoDecompilerListener();
        new ParseTreeWalker().walk(listener, parser.program());
        return listener.result();
    }


}
