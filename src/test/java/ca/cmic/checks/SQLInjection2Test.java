/*
 * Copyright (C) 2012-2022 SonarSource SA - mailto:info AT sonarsource DOT com
 * This code is released under [MIT No Attribution](https://opensource.org/licenses/MIT-0) license.
 */
package ca.cmic.checks;

import ca.cmic.rules.SQLInjection2Check;
import ca.cmic.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.CheckVerifier;


class SQLInjection2Test {
    @Test
    public void test() {
                CheckVerifier.newVerifier()
                .onFile(TestUtils.testCodeSourcesPath("src/test/files/SQLInjection2.java"))
//                .withCheck(check)
                .withCheck(new SQLInjection2Check())
                .verifyIssues();
//        CheckVerifier checkVerifier = JavaCheckVerifier.newVerifier();
//        checkVerifier.onFile("src/test/files/SQLInjection2.java");
//        checkVerifier.withCheck(new SQLInjection2Check());
//        checkVerifier.verifyIssues();
    }

}
//    @Rule
//    public LogTester logTester = new LogTester().setLevel(LoggerLevel.DEBUG);
////    private SQLInjection2Check check = new SQLInjection2Check();
//
//    @Test
//    void test() {
//        CheckVerifier.newVerifier()
//                .onFile(TestUtils.testCodeSourcesPath("src/test/files/SQLInjection2.java"))
////                .withCheck(check)
//                .withCheck(new SQLInjection2Check())
//                .verifyIssues();
//    }
//}
//
//    @Test
//    public void test() {
//        CheckVerifier.newVerifier()
//                .onFile("src/test/files/SQLInjection2.java")
//                .onFile(TestUtils.testCodeSourcesPath("src/test/files/SQLInjection2.java"))
//                .withCheck(new SQLInjection2Check())
//                .verifyIssues();

//        @Test
//        void test() {
//            CheckVerifier.newVerifier()
//                    .onFile(TestUtils.testCodeSourcesPath("src/test/files/SQLInjection2.java"))
//                    .withCheck(new SQLInjection2Check())
//                    .verifyIssues();
//
//        String vulnerableParam = "' OR 1=1--";
//        SQLInjection2Check sqlInjection2 = new SQLInjection2Check();
//        XMLEntityManager entityManager = mock(XMLEntityManager.class);
//        sqlInjection2.method(vulnerableParam, "param2", entityManager);
//    }
//    private XMLEntityManager mock(Class<XMLEntityManager> entityManagerClass) {
//        return Mockito.mock(XMLEntityManager.class);
//    }
