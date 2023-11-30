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
        System.out.printf("ENTER INSTRUCTION: %s%n", ctx.getText());
    }

    @Override
    public void exitInstruction(final DecompilerParser.InstructionContext ctx) {
        System.out.printf("EXIT INSTRUCTION: %s%n", ctx.getRuleIndex());
    }

    @Override
    public void enterLabel(final DecompilerParser.LabelContext ctx) {
        System.out.printf("\tENTER LABEL: %s%n", ctx.getText());
    }

    @Override
    public void exitLabel(final DecompilerParser.LabelContext ctx) {
        System.out.printf("\tEXIT LABEL: %s%n", ctx.getText());
    }

    @Override
    public void enterDload(final DecompilerParser.DloadContext ctx) {
        System.out.println("\t\tENTER DLOAD");
    }

    @Override
    public void exitDload(final DecompilerParser.DloadContext ctx) {
        System.out.println("\t\tEXIT DLOAD");
    }

    @Override
    public void enterDconst_0(final DecompilerParser.Dconst_0Context ctx) {
        System.out.println("\t\tENTER DCONST_0");
    }

    @Override
    public void exitDconst_0(final DecompilerParser.Dconst_0Context ctx) {
        System.out.println("\t\tEXIT DCONST_0");
    }

    @Override
    public void enterDcmpl(final DecompilerParser.DcmplContext ctx) {
        System.out.println("\t\tENTER DCMPL");
    }

    @Override
    public void exitDcmpl(final DecompilerParser.DcmplContext ctx) {
        System.out.println("\t\tEXIT DCMPL");
    }

    @Override
    public void enterIfle(final DecompilerParser.IfleContext ctx) {
        System.out.println("\t\tENTER IFLE");
    }

    @Override
    public void exitIfle(final DecompilerParser.IfleContext ctx) {
        System.out.println("\t\tEXIT IFLE");
    }

    @Override
    public void enterIconst_5(final DecompilerParser.Iconst_5Context ctx) {
        System.out.println("\t\tENTER ICONST_5");
    }

    @Override
    public void exitIconst_5(final DecompilerParser.Iconst_5Context ctx) {
        System.out.println("\t\tEXIT ICONST_5");
    }

    @Override
    public void enterIreturn(final DecompilerParser.IreturnContext ctx) {
        System.out.println("\t\tENTER IRETURN");
    }

    @Override
    public void exitIreturn(final DecompilerParser.IreturnContext ctx) {
        System.out.println("\t\tEXIT IRETURN");
    }

    @Override
    public void enterBipush(final DecompilerParser.BipushContext ctx) {
        System.out.println("\t\tENTER BIPUSH");
    }

    @Override
    public void exitBipush(final DecompilerParser.BipushContext ctx) {
        System.out.println("\t\tEXIT BIPUSH");
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
