/*
 * Copyright (C) 2012-2022 SonarSource SA - mailto:info AT sonarsource DOT com
 * This code is released under [MIT No Attribution](https://opensource.org/licenses/MIT-0) license.
 */
package ca.cmic.checks;

import ca.cmic.rules.SQLInjectionCheck;
import ca.cmic.utils.TestUtils;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.sonar.api.utils.log.LogTester;
import org.sonar.api.utils.log.LoggerLevel;
import org.sonar.java.checks.verifier.CheckVerifier;


class SQLInjectionCheckTest {
	
	  @Rule
	  public LogTester logTester = new LogTester().setLevel(LoggerLevel.DEBUG);
	
	@Test
	  void test() {
	    CheckVerifier.newVerifier()
	      .onFile(TestUtils.testCodeSourcesPath("src/test/files/SQLInjection.java"))
	      .withCheck(new SQLInjectionCheck())
	      .verifyIssues();
	  }
}
