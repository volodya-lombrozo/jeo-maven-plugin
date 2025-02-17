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
assert log.contains(" Disassembling files from ")
assert log.contains("Application.class' disassembled to")
assert log.contains("Foo.class' disassembled to ")
assert log.contains("WithoutPackage.class' disassembled to ")
assert log.contains("BUILD SUCCESS")
//Check that we have generated XMIR object file.
assert new File(basedir, 'target/generated-sources/jeo-xmir/org/eolang/jeo/Application.xmir').exists()
assert new File(basedir, 'target/generated-sources/jeo-phi/org/eolang/jeo/Application.phi').exists()
true
