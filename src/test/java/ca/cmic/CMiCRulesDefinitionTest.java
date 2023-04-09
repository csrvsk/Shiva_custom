package ca.cmic;

import org.junit.jupiter.api.Test;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.debt.DebtRemediationFunction.Type;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.Param;
import org.sonar.api.server.rule.RulesDefinition.Repository;
import org.sonar.api.server.rule.RulesDefinition.Rule;

import static org.assertj.core.api.Assertions.assertThat;

public class CMiCRulesDefinitionTest {
    @Test
    void test() {
        CMiCRulesDefinition rulesDefinition = new CMiCRulesDefinition(new RulesPluginTest.MockedSonarRuntime());
        RulesDefinition.Context context = new RulesDefinition.Context();
        rulesDefinition.define(context);
        RulesDefinition.Repository repository = context.repository(CMiCRulesDefinition.REPOSITORY_KEY);

        assertThat(repository.name()).isEqualTo(CMiCRulesDefinition.REPOSITORY_NAME);
        assertThat(repository.language()).isEqualTo("java");
        assertThat(repository.rules()).hasSize(RulesList.getChecks().size());
        assertThat(repository.rules().stream().filter(Rule::template)).isEmpty();

//        assertRuleProperties(repository);
//        assertParameterProperties(repository);
//        assertAllRuleParametersHaveDescription(repository);
    }

    private static void assertParameterProperties(Repository repository) {
        Param max = repository.rule("SQL-101").param("name");
        assertThat(max).isNotNull();
        assertThat(max.defaultValue()).isEqualTo("Inject");
        assertThat(max.description()).isEqualTo("SQL-101 : Make sure using a dynamically formatted SQL query(parameter/inline) is safe here.");
        assertThat(max.type()).isEqualTo(RuleParamType.STRING);
    }

    private static void assertRuleProperties(Repository repository) {
        Rule rule = repository.rule("SQL-101");
        assertThat(rule).isNotNull();
        assertThat(rule.name()).isEqualTo("Formatting SQL queries is security-sensitive");
        assertThat(rule.debtRemediationFunction().type()).isEqualTo(Type.CONSTANT_ISSUE);
        assertThat(rule.type()).isEqualTo(RuleType.SECURITY_HOTSPOT);
    }
	
	private static void assertRulePropertiesJava101 (Repository repository) {
        Rule rule = repository.rule("Java-101");
        assertThat(rule).isNotNull();
        assertThat(rule.name()).isEqualTo("Java-101: Avoid using the symbol '&', as it can lead to unexpected behavior.");
        assertThat(rule.debtRemediationFunction().type()).isEqualTo(Type.CONSTANT_ISSUE);
        assertThat(rule.type()).isEqualTo(RuleType.CODE_SMELL);
    }

    private static void assertRulePropertiesSQL102 (Repository repository) {
        Rule rule = repository.rule("SQL-102");
        assertThat(rule).isNotNull();
        assertThat(rule.name()).isEqualTo("SQL-102: Potential SQL Injection Vulnerability");
        assertThat(rule.debtRemediationFunction().type()).isEqualTo(Type.CONSTANT_ISSUE);
        assertThat(rule.type()).isEqualTo(RuleType.CODE_SMELL);
    }

    private static void assertAllRuleParametersHaveDescription(Repository repository) {
        for (Rule rule : repository.rules()) {
            for (Param param : rule.params()) {
                assertThat(param.description()).as("description for " + param.key()).isNotEmpty();
            }
        }
    }
}
