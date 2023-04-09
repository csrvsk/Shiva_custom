/*
 * Copyright (C) 2012-2022 SonarSource SA - mailto:info AT sonarsource DOT com
 * This code is released under [MIT No Attribution](https://opensource.org/licenses/MIT-0) license.
 */
package ca.cmic.utils;

import ca.cmic.internal.InternalInputFile;
import org.sonar.api.batch.fs.InputFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class TestUtils {
  private TestUtils() {
    // utility class, forbidden constructor
  }

  private static final String PROJECT_LOCATION = "";
  private static final String MAIN_CODE_SOURCES_DIR = PROJECT_LOCATION + "src/main/java/";
  private static final String TEST_CODE_SOURCES_DIR = PROJECT_LOCATION + "src/main/test/files/";
  private static final String NON_COMPILING_TEST_SOURCES_DIR = PROJECT_LOCATION + "src/main/files/non-compiling/";

  /**
   * @deprecated use {@link #mainCodeSourcesPath(String)}, {@link #testCodeSourcesPath(String)} instead
   */
 // @Deprecated(forRemoval = true)
  public static String testSourcesPath(String path) {
    return getFileFrom(path, MAIN_CODE_SOURCES_DIR);
  }

  /**
   * To be used when testing rules targeting MAIN code.
   */
  public static String mainCodeSourcesPath(String path) {	 
	  return getFileFrom(path, MAIN_CODE_SOURCES_DIR);
  }

  /**
   * To be used when testing rules targeting TEST code.
   */
  public static String testCodeSourcesPath(String path) {
	  //System.out.println();
	  
    return getFileFrom("", new File(path).getAbsolutePath());//TEST_CODE_SOURCES_DIR);
  }

  /**
   * To be used when testing rules behavior when bytecode is missing, partial, or code does not compile.
   */
  public static String nonCompilingTestSourcesPath(String path) {
    return getFileFrom(path, NON_COMPILING_TEST_SOURCES_DIR);
  }

  private static String getFileFrom(String path, String relocated) {
	  String filePath = relocated + path;
	  System.out.println(filePath);
    File file = new File((filePath).replace('/', File.separatorChar));
    if (!file.exists()) {
      throw new IllegalStateException("Path '" + path + "' should exist.");
    }
    try {
      return file.getCanonicalPath();
    } catch (IOException e) {
      throw new IllegalStateException("Invalid canonical path for '" + path + "'.", e);
    }
  }

  public static InputFile emptyInputFile(String filename) {
    return InternalInputFile.emptyInputFile(filename, InputFile.Type.MAIN);
  }

  public static InputFile emptyInputFile(String filename, InputFile.Type type) {
    return InternalInputFile.emptyInputFile(filename, type);
  }

  public static InputFile inputFile(String filepath) {
    return InternalInputFile.inputFile("", new File(filepath));
  }

  public static InputFile inputFile(File file) {
    return InternalInputFile.inputFile("", file);
  }

  public static final String CLASS_PATH = "target/classes";
  public static final Charset CHARSET = StandardCharsets.UTF_8;

//  public static String testCodeSourcesPath(String fileName) {
//    return "src/test/files/";
//  }

  public static InputFile inputFile(String moduleKey, File file) {
    return InternalInputFile.inputFile(moduleKey, file);
  }
}
