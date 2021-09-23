/*
 * Copyright (C) 2020 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package okio

import kotlin.test.Test
import kotlin.test.assertEquals
import okio.Path.Companion.toOkioPath
import okio.Path.Companion.toPath
import org.junit.Ignore

@ExperimentalFileSystem
class JvmPathTest {

  @Test
  @Ignore("only works on Windows??")
  fun relativeToWindowsPaths() {
    val a = "C:\\Windows\\notepad.exe".toPath()
    val b = "C:\\".toPath()
    assertRelativeTo(b, a, "..\\..".toPath())
    assertRelativeTo(a, b, "Windows\\notepad.exe".toPath())
  }

  @Test
  fun absoluteUnixRoot() {
    val a = "/Users/jesse/hello.txt".toPath()
    val b = "/".toPath()
    assertRelativeTo(b, a, "../../..".toPath())
    assertRelativeTo(a, b, "Users/jesse/hello.txt".toPath())
  }

  @Test
  fun absoluteToRelative() {
    val a = "/Users/jesse/hello.txt".toPath()
    val b = "Desktop/goodbye.txt".toPath()
    assertRelativeTo(b, a, b)
    assertRelativeTo(a, b, a)
  }

  @Test
  fun absoluteToAbsolute() {
    val a = "/Users/jesse/hello.txt".toPath()
    val b = "/Users/benoit/Desktop/goodbye.txt".toPath()
    assertRelativeTo(b, a, "../../benoit/Desktop/goodbye.txt".toPath())
    assertRelativeTo(a, b, "../../../jesse/hello.txt".toPath())
  }

  @Test
  fun dotToPath() {
    assertEquals("/a/b".toPath(), "/a/./b".toPath())
    assertEquals(".", ".".toPath().toString())
  }

  @Test
  fun absoluteToSelf() {
    val a = "/Users/jesse/hello.txt".toPath()
    assertRelativeTo(a, a, ".".toPath())
  }

  @Test
  fun relativeToSelf() {
    val a = "Desktop/hello.txt".toPath()
    assertRelativeTo(a, a, ".".toPath())
  }

  @Test
  fun relativeToRelative() {
    val a = "Desktop/documents/resume.txt".toPath()
    val b = "Desktop/documents/2021/taxes.txt".toPath()
    assertRelativeTo(b, a, "../2021/taxes.txt".toPath())
    assertRelativeTo(a, b, "../../resume.txt".toPath())
  }

  /**
   * The 'this' path is assumed to be a directory name.
   *
   * This result should hold regardless of what the current working directory is.
   *
   */
  fun assertRelativeTo(a: Path, b: Path, expected: Path) {
    assertEquals(expected.toFile(), a.toFile().relativeTo(b.toFile()))
  }
}
