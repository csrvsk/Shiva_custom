/*
 * Copyright (C) 2012-2022 SonarSource SA - mailto:info AT sonarsource DOT com
 * This code is released under [MIT No Attribution](https://opensource.org/licenses/MIT-0) license.
 */
package org.cmic.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import org.hibernate.Session;
import javax.persistence.EntityManager;

import javax.jdo.PersistenceManager;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;


class SQLInjection {
  public void method(String param, String param2, EntityManager entityManager) {

    try {
      Connection conn = DriverManager.getConnection("url", "user1", "password");
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT Lname FROM Customers WHERE Snum = 2001");
      String table = "Customers";
      rs = stmt.executeQuery("SELECT Lname FROM "+table+" WHERE Snum = " + param); // Noncompliant [[sc=30;ec=81]] {{SQL-101 : Make sure using a dynamically formatted SQL query(parameter/inline) is safe here.}}

      String query = "SELECT Lname FROM" +table+ " WHERE Snum = " + param;
      rs = stmt.executeQuery(query); // Noncompliant
    }catch (Exception e){

    }

  }
}



