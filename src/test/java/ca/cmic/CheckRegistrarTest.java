/*
 * Copyright (C) 2012-2022 SonarSource SA - mailto:info AT sonarsource DOT com
 * This code is released under [MIT No Attribution](https://opensource.org/licenses/MIT-0) license.
 */
package ca.cmic;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CheckRegistrarTest {

  @Test
  void checkNumberRules() {
    org.sonar.plugins.java.api.CheckRegistrar.RegistrarContext context = new org.sonar.plugins.java.api.CheckRegistrar.RegistrarContext();

    CheckRegistrar registrar = new CheckRegistrar();
    registrar.register(context);

    assertThat(context.checkClasses()).hasSize(3);
    assertThat(context.testCheckClasses()).hasSize(3);
  }

}
