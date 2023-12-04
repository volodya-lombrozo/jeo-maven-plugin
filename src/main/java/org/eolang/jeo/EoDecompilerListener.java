package org.eolang.jeo;


import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.objectweb.asm.Type;

public class EoDecompilerListener implements DecompilerListener {

    Deque<String> finalStack = new LinkedList<>();
    Deque<String> oStack = new LinkedList<>();


    public String result() {
        Collection<String> reversed = new LinkedList<>();
        while (!this.finalStack.isEmpty()) {
            reversed.add(this.finalStack.removeLast());
        }
        return String.join("\n", reversed);
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
    public void enterReturn(final DecompilerParser.ReturnContext ctx) {
        if (this.oStack.isEmpty()) {
            this.finalStack.push("nop > return");
        } else {
            this.finalStack.push(String.format("%s > return", this.oStack.pop()));
        }
    }

    @Override
    public void exitReturn(final DecompilerParser.ReturnContext ctx) {

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
    public void enterInvokevirtual(final DecompilerParser.InvokevirtualContext ctx) {
        final String method = ctx.SVALUE(1).getText();
        final String methodDescriptor = ctx.SVALUE(2).getText();
        final int argumentCount = Type.getArgumentCount(methodDescriptor);
        String space = "\t";
        final List<String> args = IntStream.range(0, argumentCount)
            .mapToObj(i -> this.oStack.pop()).collect(Collectors.toList());
        final String object = this.oStack.pop();
        final String invocation = String.format("%s.%s", object, method);
        final StringBuilder builder = new StringBuilder(invocation).append("\n");
        args.stream().forEach(arg -> builder.append(String.format("%s%s", space, arg)));
        this.finalStack.push(builder.toString());
    }

    @Override
    public void exitInvokevirtual(final DecompilerParser.InvokevirtualContext ctx) {

    }

    @Override
    public void enterLdc(final DecompilerParser.LdcContext ctx) {
        this.oStack.push(String.format("\"%s\"", ctx.SVALUE().getText()));
    }

    @Override
    public void exitLdc(final DecompilerParser.LdcContext ctx) {

    }

    @Override
    public void enterGetstatic(final DecompilerParser.GetstaticContext ctx) {
        this.oStack.push(
            String.format(
                "%s.%s",
                ctx.SVALUE(0).getText().replace('/', '.'), ctx.SVALUE(1)
            )
        );
    }

    @Override
    public void exitGetstatic(final DecompilerParser.GetstaticContext ctx) {

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
