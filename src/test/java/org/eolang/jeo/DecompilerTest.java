package org.eolang.jeo;

import com.jcabi.xml.XML;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import javax.tools.ToolProvider;
import org.eolang.jeo.representation.BytecodeRepresentation;
import org.eolang.jeo.representation.bytecode.Bytecode;
import org.eolang.jeo.representation.xmir.XmlMethod;
import org.eolang.jeo.representation.xmir.XmlProgram;
import org.eolang.parser.XMIR;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DecompilerTest {

    /**
     * Test of decompile method, of class Decompiler.
     * {@code
     *    private static int bar(double x) {
     *         if (x > 0.0d) {
     *             return 5;
     *         }
     *         return 8;
     *     }
     * }
     */
    private final String ifMethod =
        "            <o base=\"seq\" name=\"@\">\n"
            + "               <o base=\"label\">\n"
            + "                  <o base=\"string\" data=\"bytes\">35 62 34 39 32 33 64 32 2D 31 30 63 36 2D 34 37 66 38 2D 62 30 66 34 2D 65 62 38 35 34 31 64 30 39 63 34 62</o>\n"
            + "               </o>\n"
            + "               <o base=\"opcode\" name=\"DLOAD\">\n"
            + "                  <o base=\"int\" data=\"bytes\">00 00 00 00 00 00 00 18</o>\n"
            + "                  <o base=\"int\" data=\"bytes\">00 00 00 00 00 00 00 00</o>\n"
            + "               </o>\n"
            + "               <o base=\"opcode\" name=\"DCONST_0\">\n"
            + "                  <o base=\"int\" data=\"bytes\">00 00 00 00 00 00 00 0E</o>\n"
            + "               </o>\n"
            + "               <o base=\"opcode\" name=\"DCMPL\">\n"
            + "                  <o base=\"int\" data=\"bytes\">00 00 00 00 00 00 00 97</o>\n"
            + "               </o>\n"
            + "               <o base=\"opcode\" name=\"IFLE\">\n"
            + "                  <o base=\"int\" data=\"bytes\">00 00 00 00 00 00 00 9E</o>\n"
            + "                  <o base=\"label\">\n"
            + "                     <o base=\"string\" data=\"bytes\">38 30 31 36 63 32 62 66 2D 34 64 31 66 2D 34 35 63 32 2D 62 62 61 36 2D 31 62 63 38 34 32 64 32 36 34 62 33</o>\n"
            + "                  </o>\n"
            + "               </o>\n"
            + "               <o base=\"label\">\n"
            + "                  <o base=\"string\" data=\"bytes\">35 39 35 62 66 61 39 31 2D 33 66 37 30 2D 34 38 62 35 2D 62 35 31 63 2D 38 31 30 62 38 65 34 64 61 39 38 39</o>\n"
            + "               </o>\n"
            + "               <o base=\"opcode\" name=\"ICONST_5\">\n"
            + "                  <o base=\"int\" data=\"bytes\">00 00 00 00 00 00 00 08</o>\n"
            + "               </o>\n"
            + "               <o base=\"opcode\" name=\"IRETURN\">\n"
            + "                  <o base=\"int\" data=\"bytes\">00 00 00 00 00 00 00 AC</o>\n"
            + "               </o>\n"
            + "               <o base=\"label\">\n"
            + "                  <o base=\"string\" data=\"bytes\">38 30 31 36 63 32 62 66 2D 34 64 31 66 2D 34 35 63 32 2D 62 62 61 36 2D 31 62 63 38 34 32 64 32 36 34 62 33</o>\n"
            + "               </o>\n"
            + "               <o base=\"opcode\" name=\"BIPUSH\">\n"
            + "                  <o base=\"int\" data=\"bytes\">00 00 00 00 00 00 00 10</o>\n"
            + "                  <o base=\"int\" data=\"bytes\">00 00 00 00 00 00 00 08</o>\n"
            + "               </o>\n"
            + "               <o base=\"opcode\" name=\"IRETURN\">\n"
            + "                  <o base=\"int\" data=\"bytes\">00 00 00 00 00 00 00 AC</o>\n"
            + "               </o>\n"
            + "               <o base=\"label\">\n"
            + "                  <o base=\"string\" data=\"bytes\">36 62 31 64 63 35 31 64 2D 37 33 35 32 2D 34 31 35 66 2D 61 65 36 61 2D 33 64 63 33 62 37 36 37 32 30 65 62</o>\n"
            + "            </o>\n"
            + "         </o>";
    /**
     * Test of decompile method, of class Decompiler.
     * {@code
     * System.out.println("Hello, World!");
     * }
     */
    private String helloword =
        "            <o base=\"seq\" name=\"@\">\n"
            + "   <o base=\"opcode\" name=\"GETSTATIC\">\n"
            + "                  <o base=\"int\" data=\"bytes\">00 00 00 00 00 00 00 B2</o>\n"
            + "                  <o base=\"string\" data=\"bytes\">6A 61 76 61 2F 6C 61 6E 67 2F 53 79 73 74 65 6D</o>\n"
            + "                  <o base=\"string\" data=\"bytes\">6F 75 74</o>\n"
            + "                  <o base=\"string\" data=\"bytes\">4C 6A 61 76 61 2F 69 6F 2F 50 72 69 6E 74 53 74 72 65 61 6D 3B</o>\n"
            + "               </o>\n"
            + "               <o base=\"opcode\" name=\"LDC\">\n"
            + "                  <o base=\"int\" data=\"bytes\">00 00 00 00 00 00 00 12</o>\n"
            + "                  <o base=\"string\" data=\"bytes\">48 65 6C 6C 6F 2C 20 57 6F 72 6C 64 21</o>\n"
            + "               </o>\n"
            + "               <o base=\"opcode\" name=\"INVOKEVIRTUAL\">\n"
            + "                  <o base=\"int\" data=\"bytes\">00 00 00 00 00 00 00 B6</o>\n"
            + "                  <o base=\"string\" data=\"bytes\">6A 61 76 61 2F 69 6F 2F 50 72 69 6E 74 53 74 72 65 61 6D</o>\n"
            + "                  <o base=\"string\" data=\"bytes\">70 72 69 6E 74 6C 6E</o>\n"
            + "                  <o base=\"string\" data=\"bytes\">28 4C 6A 61 76 61 2F 6C 61 6E 67 2F 53 74 72 69 6E 67 3B 29 56</o>\n"
            + "               </o>\n"
            + "               <o base=\"opcode\" name=\"RETURN\">\n"
            + "                  <o base=\"int\" data=\"bytes\">00 00 00 00 00 00 00 B1</o>\n"
            + "               </o>"
            + "         </o>";


    @Test
    void decompileIfMethod() {
        final String res = new Decompiler().decompile(this.ifMethod);
        System.out.println(res);
        assertEquals(
            "private static int bar(double x) {\n"
                + "        if (x > 0.0d) {\n"
                + "            return 5;\n"
                + "        }\n"
                + "        return 8;\n"
                + "    }\n",
            res
        );
    }

    @Test
    void helloworld() {
        final String res = new Decompiler().decompile(this.helloword);
        System.out.printf("Final result: %n%s", res);
        MatcherAssert.assertThat(
            "Can't decompile System.out.println(\"Hello, World!\");",
            res,
            Matchers.equalTo(
                String.join(
                    "\n",
                    "java.lang.System.out.println",
                    "\t\"Hello, World!\"",
                    "nop > return"
                )
            )
        );
    }

    @Test
    void decompileDirectlyFromJavaCode(@TempDir Path temp) throws IOException {
        this.decompileJavaMethodWithContent(temp, "System.out.println(\"Wake up, Neo...\");");
    }

    @Test
    void decompileAssainment(@TempDir Path temp) throws IOException {
        this.decompileJavaMethodWithContent(temp, "int x = 123;");
    }



    private void decompileJavaMethodWithContent(@TempDir Path dir, String... content) throws IOException {
        final Bytecode application = DecompilerTest.compile(
            dir,
            "Application",
            this.frameApp(content)
        );
        final XML representation = new BytecodeRepresentation(application).toEO();
        System.out.println(new XMIR(representation).toEO());
        final List<XmlMethod> methods = new XmlProgram(representation).top().methods()
            .stream().filter(m -> !m.isConstructor()).collect(Collectors.toList());
        System.out.println("Methods: " + methods.size());
        System.out.println("DECOMPILED METHOD:\n");
        System.out.println(new Decompiler().decompile(methods.get(0)));
    }


    private String frameApp(final String... methodContent) {
        return String.join(
            "\n",
            "public class Application{",
            "public static void main(String... args){",
            String.join("\n", methodContent),
            "}",
            "}"

        );
    }

    private static Bytecode compile(
        final Path where,
        final String name,
        final String source
    ) throws IOException {
        final Path src = where.resolve(String.format("%s.java", name));
        Files.write(src, source.getBytes(StandardCharsets.UTF_8));
        ToolProvider.getSystemJavaCompiler().run(
            System.in,
            System.out,
            System.err,
            "-g:none",
            "-source", "11",
            "-target", "11",
            src.toString()
        );
        return new Bytecode(
            Files.readAllBytes(where.resolve(String.format("%s.class", name)))
        );
    }
}