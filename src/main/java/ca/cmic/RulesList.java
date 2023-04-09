/*
 * Copyright (C) 2012-2022 SonarSource SA - mailto:info AT sonarsource DOT com
 * This code is released under [MIT No Attribution](https://opensource.org/licenses/MIT-0) license.
 */
package ca.cmic;

import ca.cmic.rules.AvoidANDSymbolCheck;
import ca.cmic.rules.SQLInjection2Check;
import ca.cmic.rules.SQLInjectionCheck;
import org.sonar.plugins.java.api.JavaCheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public final class RulesList {

  private RulesList() {
  }

  public static List<Class<? extends JavaCheck>> getChecks() {
    List<Class<? extends JavaCheck>> checks = new ArrayList<>();
    checks.addAll(getJavaChecks());
    //checks.addAll(getJavaTestChecks());
    return Collections.unmodifiableList(checks);
  }

  /**
   * These rules are going to target MAIN code only
   */
  public static List<Class<? extends JavaCheck>> getJavaChecks() {
    return Collections.unmodifiableList(Arrays.asList(
      SQLInjectionCheck.class,
      AvoidANDSymbolCheck.class,
      SQLInjection2Check.class
      ));
  }

  /**
   * These rules are going to target TEST code only
   */
}




//following list is for example rules & their import statements
//import ca.cmic.examplerules.AvoidAnnotationRule;
//import ca.cmic.examplerules.AvoidBrandInMethodNamesRule;
//import ca.cmic.examplerules.AvoidMethodDeclarationRule;
//import ca.cmic.examplerules.AvoidSuperClassRule;
//import ca.cmic.examplerules.AvoidTreeListRule;
//import ca.cmic.examplerules.MyCustomSubscriptionRule;
//import ca.cmic.examplerules.MyFirstCustomCheck;
//import ca.cmic.examplerules.SecurityAnnotationMandatoryRule;
//import ca.cmic.examplerules.SpringControllerRequestMappingEntityRule;

//SpringControllerRequestMappingEntityRule.class,
//AvoidAnnotationRule.class,
//AvoidBrandInMethodNamesRule.class,
//AvoidMethodDeclarationRule.class,
//AvoidSuperClassRule.class,
//AvoidTreeListRule.class,
//MyCustomSubscriptionRule.class,
//SecurityAnnotationMandatoryRule.class,
//MyFirstCustomCheck.class,
