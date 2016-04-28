package net.alteiar.db.installer.xml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import net.alteiar.db.installer.xml.exception.ParsingException;
import net.alteiar.sql.SqlScriptType;

public class SqlScriptWrapperTest {

  @Test(expected = ParsingException.class)
  public void testInvalid() throws Exception {

    InputStream invalidScript = getClass().getResourceAsStream("/xml/invalid/script_invalid.xml");
    new SqlScriptXml(invalidScript);
  }

  @Test
  public void testValid() throws Exception {

    InputStream validScript = getClass().getResourceAsStream("/xml/script.xml");
    SqlScriptXml wrapper = new SqlScriptXml(validScript);

    Assert.assertEquals("validate name", "create db", wrapper.getName());
    Assert.assertEquals("validate description", "Here a description about the query", wrapper.getDescription());
    Assert.assertEquals("validate query", "CREATE TABLE test (id int(10), name varchar(120), description varchar(250))",
                        wrapper.getQuery());

  }

  @Test
  public void testValidWithScript() throws Exception {

    String expectedName = "create db 2";
    String expectedDescription = "Another description";
    String expectedQuery = "CREATE TABLE abc (id int(10), name varchar(120), description varchar(250))";

    SqlScriptType script = new SqlScriptType();
    script.setName(expectedName);
    script.setDescription(expectedDescription);
    script.setQuery(expectedQuery);

    SqlScriptXml wrapper = new SqlScriptXml(script);

    Assert.assertEquals("validate name", expectedName, wrapper.getName());
    Assert.assertEquals("validate description", expectedDescription, wrapper.getDescription());
    Assert.assertEquals("validate query", expectedQuery, wrapper.getQuery());
  }

  @Test
  public void testValidRead() throws Exception {

    InputStream validScript = getClass().getResourceAsStream("/xml/script.xml");
    SqlScriptXml wrapper = new SqlScriptXml(validScript);

    Assert.assertEquals("validate name", "create db", wrapper.getName());
    Assert.assertEquals("validate description", "Here a description about the query", wrapper.getDescription());
    Assert.assertEquals("validate query", "CREATE TABLE test (id int(10), name varchar(120), description varchar(250))",
                        wrapper.getQuery());

    List<String> lines = Files.readAllLines(Paths.get(getClass().getResource("/xml/script.xml").toURI()));

    StringBuilder file = new StringBuilder();

    for (String line : lines) {
      file.append(line.trim());
    }

    Assert.assertEquals(file.toString(), wrapper.toString());
  }
}
