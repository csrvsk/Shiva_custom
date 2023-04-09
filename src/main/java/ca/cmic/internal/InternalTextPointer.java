/*
 * Copyright (C) 2012-2022 SonarSource SA - mailto:info AT sonarsource DOT com
 * This code is released under [MIT No Attribution](https://opensource.org/licenses/MIT-0) license.
 */
package ca.cmic.internal;

import java.util.Comparator;
import org.sonar.api.batch.fs.TextPointer;

import static java.util.Comparator.comparingInt;

final class InternalTextPointer implements TextPointer {
  private static final Comparator<TextPointer> COMPARATOR = comparingInt(TextPointer::line).thenComparing(TextPointer::lineOffset);
  private final int line;
  private final int offset;

  InternalTextPointer(int line, int offset) {
    this.line = line;
    this.offset = offset;
  }

  @Override
  public int line() {
    return line;
  }

  @Override
  public int lineOffset() {
    return offset;
  }

  @Override
  public int compareTo(TextPointer o) {
    return COMPARATOR.compare(this, o);
  }
}
