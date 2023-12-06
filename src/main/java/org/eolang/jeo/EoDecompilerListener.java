package org.eolang.jeo;


import com.google.common.base.Strings;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashMap;
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

    /**
     * Stack with output.
     */
    private Deque<Printable> finalStack = new LinkedList<>();

    /**
     * Operand stack.
     */
    private Deque<String> oStack = new LinkedList<>();


    /**
     * Reference counter.
     */
    private AtomicInteger referenceCounter = new AtomicInteger(0);

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
        final String methodName = ctx.SVALUE(1).getText();
        final String methodDescriptor = ctx.SVALUE(2).getText();
        final int argumentCount = Type.getArgumentCount(methodDescriptor);
        final List<Printable> args = IntStream.range(0, argumentCount)
            .mapToObj(i -> {
                final Printable obj = this.argument(this.oStack.pop());
                if (obj.equals(this.finalStack.peek())) {
                    this.finalStack.pop();
                }
                return obj;
            })
            .collect(Collectors.toList());
        final Printable object = this.argument(this.oStack.pop());
        if (object.equals(this.finalStack.peek())) {
            this.finalStack.pop();
        }
        final Method method = new Method(object, methodName, args);
        this.output(method);
        final Type returnType = Type.getReturnType(methodDescriptor);
        if (!returnType.equals(Type.VOID_TYPE)) {
            final String reference = this.reference(returnType.getClassName());
            this.oStack.push(reference);
            this.objectsStack.put(reference, method);
        }
    }

    private String reference(final String type) {
        return String.format("%s%d%s",
            "&",
            this.referenceCounter.getAndIncrement(),
            type.replace('/', '.')
        );
    }

    private Printable argument(final String stackIdentifier) {
        if (this.objectsStack.containsKey(stackIdentifier)) {
            return this.objectsStack.get(stackIdentifier);
        } else {
            return new SimpleNode(stackIdentifier);
        }
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
                    final Printable obj = this.argument(this.oStack.pop());
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
        this.oStack.push(this.reference(ctx.SVALUE().getText()));
    }

    @Override
    public void exitNew(final DecompilerParser.NewContext ctx) {

    }

    @Override
    public void enterStore(final DecompilerParser.StoreContext ctx) {
        if (!this.oStack.isEmpty()) {
            final String type = ctx.TYPE().toString();
            final String n = ctx.IVALUE().getText();
            this.output(String.format("%s > local%s", this.oStack.pop(), n));
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
        this.output(
            new OpcodeNode(
                ctx.UNDEFINED().getText(),
                IntStream.range(0, ctx.getChildCount() - 1)
                    .mapToObj(i -> ctx.SVALUE(i).getText())
                    .collect(Collectors.toList())
            )
        );
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

    private void output(final String output) {
        this.finalStack.push(new SimpleNode(output));
    }

    private void output(final Printable printable) {
        this.finalStack.push(printable);
    }


    private interface Printable {

        String print(int indent);
    }

    private static class Method implements Printable {
        private final Printable target;
        private final String method;
        private final List<Printable> args;

        public Method(final Printable target, final String method, Printable... args) {
            this(target, method, Arrays.asList(args));
        }

        public Method(final Printable target, final String method, final List<Printable> args) {
            this.target = target;
            this.method = method;
            this.args = args;
        }

        @Override
        public String print(final int indent) {
            return new Spaces(indent) + "(" + this.target.print(indent) + ")." + this.method
                + "\n"
                +
                this.args.stream().map(a -> a.print(indent + 2)).collect(Collectors.joining("\n"));
        }
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

    private static class OpcodeNode implements Printable {

        private final String opcode;
        private final List<String> arguments;

        public OpcodeNode(final String opcode, final List<String> arguments) {
            this.opcode = opcode;
            this.arguments = arguments;
        }

        private String args(int indent) {
            if (this.arguments.isEmpty()) {
                return "";
            }
            return this.arguments.stream()
                .map(arg -> new Spaces(indent + 2) + arg)
                .collect(Collectors.joining("\n", "\n", ""));
        }

        @Override
        public String print(final int indent) {
            return String.format(
                "%sopcode > %s%s",
                new Spaces(indent),
                new OpcodeName(Integer.parseInt(this.opcode)).asString(),
                this.args(indent)
            );
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
            return Strings.repeat(" ", this.indent);
        }
    }
}
