package ca.cmic.checks;

import ca.cmic.rules.AvoidANDSymbolCheck;
import ca.cmic.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.CheckVerifier;
public class AvoidANDSymbolTest {
    @Test
    void test() {
        String fileName = "src/test/files/AvoidANDSymbol.java";
        CheckVerifier.newVerifier()
                .onFile(TestUtils.testCodeSourcesPath(fileName))
                .withCheck(new AvoidANDSymbolCheck())
                .verifyIssues();
    }
}
