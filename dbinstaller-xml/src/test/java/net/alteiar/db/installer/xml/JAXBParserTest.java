package net.alteiar.db.installer.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import net.alteiar.db.installer.xml.exception.ParsingException;
import net.alteiar.sql.ObjectFactory;
import net.alteiar.sql.SqlModuleType;

public class JAXBParserTest {

  @Test
  public void testUnmarshall() throws ParsingException {

    JAXBParser parser = JAXBParser.getInstance();

    SqlModuleType module = (SqlModuleType) parser.unmarshall(getClass().getResourceAsStream("/xml/module.xml"));

    Assert.assertEquals("validate name", "test", module.getName());
    Assert.assertEquals("validate scripts", "create db", module.getScript().get(0));
    Assert.assertEquals("validate scripts", 4, module.getScript().size());
  }

  @Test
  public void testUnmarshallInvalid() throws ParsingException {

    JAXBParser parser = JAXBParser.getInstance();

    try {

      parser.unmarshall(getClass().getResourceAsStream("/xml/invalid/script_invalid.xml"));
      Assert.fail("A parsing exception must occured");
    } catch (ParsingException ex) {

      Assert.assertEquals("Failed to unmarshall the input", ex.getMessage());
    }
  }

  @Test
  public void testMarshall() throws ParsingException {

    JAXBParser parser = JAXBParser.getInstance();

    SqlModuleType module = new SqlModuleType();
    module.setName("test");
    module.getScript().add("create db");
    module.getScript().add("create db1");
    module.getScript().add("create db2");
    module.getScript().add("create db3");

    ObjectFactory factory = new ObjectFactory();

    String xml = parser.marshall(factory.createSqlModule(module));

    InputStream is = new ByteArrayInputStream(xml.getBytes());
    SqlModuleType moduleUnmarshalled = (SqlModuleType) parser.unmarshall(is);

    Assert.assertEquals("validate name", "test", moduleUnmarshalled.getName());
    Assert.assertEquals("validate scripts", "create db", moduleUnmarshalled.getScript().get(0));
    Assert.assertEquals("validate scripts", 4, moduleUnmarshalled.getScript().size());
  }

}
