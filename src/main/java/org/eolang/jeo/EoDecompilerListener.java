package org.eolang.jeo;


import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eolang.jeo.representation.directives.OpcodeName;
import org.objectweb.asm.Type;

public class EoDecompilerListener implements DecompilerListener {

    /**
     * Stack with output.
     */
    private Deque<Printable> finalStack = new LinkedList<>();

    /**
     * Operand stack.
     */
    private Deque<String> oStack = new LinkedList<>();

    /**
     * Object reference stack.
     */
    private LinkedHashMap<String, Printable> objectsStack = new LinkedHashMap<>();

    private String space = "  ";


    public String result() {
        Collection<String> reversed = new LinkedList<>();
        while (!this.finalStack.isEmpty()) {
            reversed.add(this.finalStack.removeLast().print(0));
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
            this.output("nop > return");
        } else {
            this.output(String.format("%s > return", this.oStack.pop()));
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
        args.stream().forEach(arg -> builder.append(String.format("%s%s", this.space(), arg)));
        this.output(builder.toString());
    }

    @Override
    public void exitInvokevirtual(final DecompilerParser.InvokevirtualContext ctx) {
    }

    @Override
    public void enterInvokespecial(final DecompilerParser.InvokespecialContext ctx) {
        final String code = ctx.INVOKESPECIAL().getText();
        final String object = ctx.SVALUE(0).getText();
        final String mname = ctx.SVALUE(1).getText();
        final String descriptor = ctx.SVALUE(2).getText();
        if (mname.equals("<init>")) {
            final int argumentCount = Type.getArgumentCount(descriptor);
            final List<Printable> args = IntStream.range(0, argumentCount)
                .mapToObj(i -> {
                    final String arg = this.oStack.pop();
                    final Printable obj = this.objectsStack.getOrDefault(arg, new SimpleNode(arg));
                    if (obj.equals(this.finalStack.peek())) {
                        this.finalStack.pop();
                    }
                    return obj;
                }).collect(Collectors.toList());
            final String obj = this.oStack.pop();
            final Printable constructor = new Constructor(object, args);
            this.objectsStack.put(obj, constructor);
            this.output(constructor);
        } else {
            throw new IllegalStateException(
                String.format(
                    "Unknown invokespecial instruction code=%s, object=%s, name=%s, descriptor=%s",
                    code,
                    object,
                    mname,
                    descriptor
                )
            );
        }


    }

    @Override
    public void exitInvokespecial(final DecompilerParser.InvokespecialContext ctx) {

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
        this.oStack.push(ctx.SVALUE().getText().replace('/', '.') + ".new");
    }

    @Override
    public void exitNew(final DecompilerParser.NewContext ctx) {

    }

    @Override
    public void enterStore(final DecompilerParser.StoreContext ctx) {
        if (!this.oStack.isEmpty()) {
            final String type = ctx.TYPE().toString();
            final String n = ctx.IVALUE().getText();
            this.output(String.format("%s > local%s // %s", this.oStack.pop(), n, type));
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
    public void enterDup(final DecompilerParser.DupContext ctx) {
//        Doesn't work for some reason?
        this.oStack.push(this.oStack.peek());
    }

    @Override
    public void exitDup(final DecompilerParser.DupContext ctx) {

    }

    @Override
    public void enterPop(final DecompilerParser.PopContext ctx) {
//        Pop doesn't work for some reason?
        this.oStack.pop();
    }

    @Override
    public void exitPop(final DecompilerParser.PopContext ctx) {

    }

    @Override
    public void enterUndefined(final DecompilerParser.UndefinedContext ctx) {
        this.output(new StringBuilder("opcode")
            .append(" > ")
            .append(new OpcodeName(Integer.parseInt(ctx.UNDEFINED().getText())).asString())
            .append("\n")
            .append(
                IntStream.range(0, ctx.getChildCount() - 1)
                    .mapToObj(i -> this.space() + ctx.SVALUE(i).getText())
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

    private String space() {
        return this.space;
    }


    private void output(final String output) {
        this.finalStack.push(new SimpleNode(output));
    }

    private void output(final Printable printable) {
        this.finalStack.push(printable);
    }


    private interface Printable {

        String print(int indent);
    }


    private static class Constructor implements Printable {
        private final String type;
        private final List<Printable> args;

        public Constructor(final String type, Printable... args) {
            this(type, Arrays.asList(args));
        }

        public Constructor(final String type, final List<Printable> args) {
            this.type = type;
            this.args = args;
        }

        @Override
        public String print(int indent) {
            return new Spaces(indent) + this.type.replace('/', '.') + ".new\n" +
                this.args.stream().map(a -> a.print(indent + 2))
                    .collect(Collectors.joining("\n"));
        }
    }

    private static class SimpleNode implements Printable {

        private final String line;

        private SimpleNode(final String line) {
            this.line = line;
        }

        @Override
        public String print(int indent) {
            return new Spaces(indent) + this.line;
        }
    }

    private static class Spaces {

        private final int indent;

        public Spaces(final int indent) {
            this.indent = indent;
        }

        @Override
        public String toString() {
            return IntStream.range(0, this.indent).mapToObj(i -> " ").collect(Collectors.joining());
        }
    }
}
