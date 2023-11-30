package org.eolang.jeo;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

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


}
