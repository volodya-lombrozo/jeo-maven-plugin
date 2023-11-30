package org.eolang.jeo;


import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class EoDecompilerListener implements DecompilerListener {

    public String result() {
        return "yepyep";
    }

    @Override
    public void enterProgram(final DecompilerParser.ProgramContext ctx) {

    }

    @Override
    public void exitProgram(final DecompilerParser.ProgramContext ctx) {

    }

    @Override
    public void enterInstruction(final DecompilerParser.InstructionContext ctx) {

    }

    @Override
    public void exitInstruction(final DecompilerParser.InstructionContext ctx) {

    }

    @Override
    public void visitTerminal(final TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(final ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(final ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(final ParserRuleContext parserRuleContext) {

    }
}
