package org.eolang.jeo;


import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eolang.jeo.representation.directives.OpcodeName;
import org.objectweb.asm.Type;

public class EoDecompilerListener implements DecompilerListener {

    Deque<String> finalStack = new LinkedList<>();
    Deque<String> oStack = new LinkedList<>();

    AtomicInteger variableCounter = new AtomicInteger(0);

    private String space = "\t";


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
    }

    @Override
    public void exitInstruction(final DecompilerParser.InstructionContext ctx) {
    }

    @Override
    public void enterLabel(final DecompilerParser.LabelContext ctx) {
    }

    @Override
    public void exitLabel(final DecompilerParser.LabelContext ctx) {
    }

    @Override
    public void enterDload(final DecompilerParser.DloadContext ctx) {
    }

    @Override
    public void exitDload(final DecompilerParser.DloadContext ctx) {
    }

    @Override
    public void enterDconst_0(final DecompilerParser.Dconst_0Context ctx) {
    }

    @Override
    public void exitDconst_0(final DecompilerParser.Dconst_0Context ctx) {
    }

    @Override
    public void enterDcmpl(final DecompilerParser.DcmplContext ctx) {
    }

    @Override
    public void exitDcmpl(final DecompilerParser.DcmplContext ctx) {
    }

    @Override
    public void enterIfle(final DecompilerParser.IfleContext ctx) {
    }

    @Override
    public void exitIfle(final DecompilerParser.IfleContext ctx) {
    }

    @Override
    public void enterIconst_5(final DecompilerParser.Iconst_5Context ctx) {
    }

    @Override
    public void exitIconst_5(final DecompilerParser.Iconst_5Context ctx) {
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
        this.oStack.push(ctx.IVALUE().getText());
    }

    @Override
    public void exitBipush(final DecompilerParser.BipushContext ctx) {
    }

    @Override
    public void enterInvokevirtual(final DecompilerParser.InvokevirtualContext ctx) {
        final String method = ctx.SVALUE(1).getText();
        final String methodDescriptor = ctx.SVALUE(2).getText();
        final int argumentCount = Type.getArgumentCount(methodDescriptor);
        final List<String> args = IntStream.range(0, argumentCount)
            .mapToObj(i -> this.oStack.pop()).collect(Collectors.toList());
        final String object = this.oStack.pop();
        final String invocation = String.format("%s.%s", object, method);
        final StringBuilder builder = new StringBuilder(invocation).append("\n");
        args.stream().forEach(arg -> builder.append(String.format("%s%s", this.space, arg)));
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
    public void enterNew(final DecompilerParser.NewContext ctx) {
        this.oStack.push(ctx.SVALUE().getText().replace('/', '.'));
    }

    @Override
    public void exitNew(final DecompilerParser.NewContext ctx) {

    }

    @Override
    public void enterStore(final DecompilerParser.StoreContext ctx) {
        if (!this.oStack.isEmpty()) {
            final String type = ctx.TYPE().toString();
            final String n = ctx.IVALUE().getText();
            this.finalStack.push(String.format("%s > local%s // %s", this.oStack.pop(), n, type));
        }
    }

    @Override
    public void exitStore(final DecompilerParser.StoreContext ctx) {

    }

    @Override
    public void enterLoad(final DecompilerParser.LoadContext ctx) {
        // here we can also check the type of loaded variable. Moreover, we can check if variable is already loaded
        // Since we work with eo, we can omit types, but for the back transformation to EO we need to know the type.
        this.oStack.push(String.format("local%s", ctx.IVALUE().getText()));
    }

    @Override
    public void exitLoad(final DecompilerParser.LoadContext ctx) {

    }

    @Override
    public void enterUndefined(final DecompilerParser.UndefinedContext ctx) {
        this.finalStack.push(new StringBuilder("opcode")
            .append(" > ")
            .append(new OpcodeName(Integer.parseInt(ctx.UNDEFINED().getText())).asString())
            .append("\n")
            .append(
                IntStream.range(0, ctx.getChildCount() - 1)
                    .mapToObj(i -> this.space + ctx.SVALUE(i).getText())
                    .collect(Collectors.joining("\n"))
            ).toString());
    }

    @Override
    public void exitUndefined(final DecompilerParser.UndefinedContext ctx) {

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
