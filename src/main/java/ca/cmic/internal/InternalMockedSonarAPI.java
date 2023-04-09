/*
 * Copyright (C) 2012-2022 SonarSource SA - mailto:info AT sonarsource DOT com
 * This code is released under [MIT No Attribution](https://opensource.org/licenses/MIT-0) license.
 */
package ca.cmic.internal;

class InternalMockedSonarAPI {

  private final Class<?> clazz;

  InternalMockedSonarAPI() {
    clazz = this.getClass();
  }

  NotSupportedException notSupportedException(String methodSignature) {
    return new NotSupportedException(clazz, methodSignature);
  }

  static final class NotSupportedException extends RuntimeException {

    private static final long serialVersionUID = 6465870479166535810L;
    private static final String EXCEPTION_MESSAGE = "Method unsuported by the rule verifier framework: '%s::%s'";

    private NotSupportedException(Class<?> clazz, String methodSignature) {
      super(String.format(EXCEPTION_MESSAGE, clazz.getSimpleName(), methodSignature));
    }
  }
}
