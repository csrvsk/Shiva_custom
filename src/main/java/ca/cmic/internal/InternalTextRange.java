/*
 * Copyright (C) 2012-2022 SonarSource SA - mailto:info AT sonarsource DOT com
 * This code is released under [MIT No Attribution](https://opensource.org/licenses/MIT-0) license.
 */
package ca.cmic.internal;

import org.sonar.api.batch.fs.TextPointer;
import org.sonar.api.batch.fs.TextRange;

final class InternalTextRange extends InternalMockedSonarAPI implements TextRange {
  private final TextPointer start;
  private final TextPointer end;

  InternalTextRange(int startLine, int startColumn, int endLine, int endColumn) {
    this(new InternalTextPointer(startLine, startColumn), new InternalTextPointer(endLine, endColumn));
  }

  InternalTextRange(TextPointer start, TextPointer end) {
    this.start = start;
    this.end = end;
  }

  @Override
  public TextPointer end() {
    return end;
  }

  @Override
  public TextPointer start() {
    return start;
  }

  @Override
  public boolean overlap(TextRange arg0) {
    throw notSupportedException("overlap(TextRange)");
  }
}
