/*
 * MIT License
 *
 * Copyright (c) 2016-2025 Objectionary.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
//Check logs first.
String log = new File(basedir, 'build.log').text;
assert log.contains("BUILD SUCCESS")
assert log.contains("Glad to see you, Spring...")
//Check that we have generated EO object files.
assert new File(basedir, 'target/generated-sources/jeo-xmir/org/eolang/jeo/spring/Application.xmir').exists()
assert new File(basedir, 'target/generated-sources/jeo-xmir/org/eolang/jeo/spring/Receptionist.xmir').exists()
def app = new File(basedir, 'target/generated-sources/jeo-xmir/org/eolang/jeo/spring/App.xmir')
assert app.exists()
assert app.text.contains("metas")
assert app.text.contains("<head>package</head>")
//Check that if class doesn't have instructions, it doesn't have "opcode" imports.
def empty = new File(basedir, 'target/generated-sources/jeo-xmir/org/eolang/jeo/spring/WithoutInstructions.xmir')
assert empty.exists()
true
