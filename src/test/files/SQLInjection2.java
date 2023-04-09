/*
 * Copyright (C) 2012-2022 SonarSource SA - mailto:info AT sonarsource DOT com
 * This code is released under [MIT No Attribution](https://opensource.org/licenses/MIT-0) license.
 */
package ca.cmic.utils;

import ca.cmic.rules.SQLInjectionCheck;
import ca.cmic.utils.TestUtils;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.sonar.api.utils.log.LogTester;
import org.sonar.api.utils.log.LoggerLevel;
import org.sonar.java.checks.verifier.CheckVerifier;

public class SQLInjection2 {

    public void testMethod() {
        String documentCode = "1234";
        String whereClause = "R_DOC_CODE = " + "'" + documentCode + "'"; // Noncompliant [[sc=30;ec=81]] {{SQL-102 : Make sure using a dynamically formatted SQL query(parameter/inline) is safe here.}}
        // do something with whereClause
    }
}
//    public void method(String param, String param2, EntityManager entityManager) {
//
//        try {
//            Connection conn = DriverManager.getConnection("url", "user1", "password");
//            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT Lname FROM Customers WHERE Snum = 2001");
//            String table = "Customers";
//            rs = stmt.executeQuery("SELECT Lname FROM " + getdocumentCode() + " WHERE Snum = " + param); // Noncompliant [[sc=30;ec=81]] {{SQL-102 : Make sure using a dynamically formatted SQL query(parameter/inline) is safe here.}}
//
//            String query = "SELECT Lname FROM" + getdocumentCode() + " WHERE Snum = " + param;
//            rs = stmt.executeQuery(query); // Noncompliant
//        } catch (Exception e) {
//
//        }
//
//    }
//    public String getdocumentCode() {
//        return "Customers";
//    }