package net.alteiar.db.installer.xml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import net.alteiar.db.installer.exception.ParsingException;
import net.alteiar.sql.SqlModuleType;

public class SqlModuleWrapperTest {

  @Test(expected = ParsingException.class)
  public void testInvalid() throws Exception {

    InputStream invalidModule = getClass().getResourceAsStream("/xml/invalid/module_invalid.xml");
    new SqlModuleXml(invalidModule);
  }

  @Test
  public void testValid() throws Exception {

    InputStream validModule = getClass().getResourceAsStream("/xml/module.xml");
    SqlModuleXml wrapper = new SqlModuleXml(validModule);

    SqlModuleType module = wrapper.getSqlModule();
    Assert.assertEquals("validate name", "test", module.getName());
    Assert.assertEquals("validate scripts", "create db", module.getScript().get(0));
    Assert.assertEquals("validate scripts", 4, module.getScript().size());

    wrapper = new SqlModuleXml(module);

    module = wrapper.getSqlModule();
    Assert.assertEquals("validate name", "test", module.getName());
    Assert.assertEquals("validate scripts", "create db", module.getScript().get(0));
    Assert.assertEquals("validate scripts", 4, module.getScript().size());

    List<String> lines = Files.readAllLines(Paths.get(getClass().getResource("/xml/module.xml").toURI()));

    StringBuilder file = new StringBuilder();

    for (String line : lines) {
      file.append(line.trim());
    }

    Assert.assertEquals(file.toString(), wrapper.toString());
  }

  @Test
  public void testValidRead() throws Exception {

    InputStream validModule = getClass().getResourceAsStream("/xml/module.xml");
    SqlModuleXml wrapper = new SqlModuleXml(validModule);

    SqlModuleType module = wrapper.getSqlModule();
    Assert.assertEquals("validate name", "test", module.getName());
    Assert.assertEquals("validate scripts", "create db", module.getScript().get(0));
    Assert.assertEquals("validate scripts", 4, module.getScript().size());

    List<String> lines = Files.readAllLines(Paths.get(getClass().getResource("/xml/module.xml").toURI()));

    StringBuilder file = new StringBuilder();

    for (String line : lines) {
      file.append(line.trim());
    }

    Assert.assertEquals(file.toString(), wrapper.toString());
  }
}
