package org.goodiemania.books.services.misc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StringEscapeUtilsTest {
  StringEscapeUtils escapeUtils;

  @BeforeEach
  void setUp() {
    escapeUtils = new StringEscapeUtils();
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  public void initialTest() {
    final String initialString = "<name>Eug&eacute;nie Shinkle</name>";
    final String escapedString = escapeUtils.escapeHtmlEntitiesInXml(initialString);
    Assertions.assertEquals("<name>Eugénie Shinkle</name>", escapedString);
  }

  @Test
  public void plainTextPassThrough() {
    final String initialString = "<name>Eugenie Shinkle</name>";
    final String escapedString = escapeUtils.escapeHtmlEntitiesInXml(initialString);
    Assertions.assertEquals("<name>Eugenie Shinkle</name>", escapedString);
  }
}