package net.alteiar.db.installer.xml;

import org.junit.Assert;
import org.junit.Test;

import net.alteiar.db.installer.exception.ParsingException;
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
}
