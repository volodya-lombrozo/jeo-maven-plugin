package org.eolang.jeo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
    private final String barMethod = "<o abstract=\"\" name=\"bar\">\n"
        + "            <o base=\"int\" data=\"bytes\" name=\"access\">00 00 00 00 00 00 00 0A</o>\n"
        + "            <o base=\"string\" data=\"bytes\" name=\"descriptor\">28 44 29 49</o>\n"
        + "            <o base=\"string\" data=\"bytes\" name=\"signature\"/>\n"
        + "            <o base=\"tuple\" data=\"tuple\" name=\"exceptions\"/>\n"
        + "            <o abstract=\"\" name=\"arg__D__0\"/>\n"
        + "            <o base=\"seq\" name=\"@\">\n"
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
        + "               </o>\n"
        + "            </o>\n"
        + "         </o>";


    @Test
    void decompileMethod() {
        final String res = new Decompiler().decompile(this.barMethod);
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


}