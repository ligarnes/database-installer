package net.alteiar.db.installer.xml;

import net.alteiar.db.installer.exception.ParsingException;
import net.alteiar.sql.SqlModule;

import org.junit.Assert;
import org.junit.Test;

public class JAXBParserTest {

    @Test
    public void testUnmarshall() throws ParsingException {

        JAXBParser parser = JAXBParser.getInstance();

        SqlModule module = (SqlModule) parser.unmarshall(getClass().getResourceAsStream("/xml/module.xml"));

        Assert.assertEquals("validate name", "test", module.getName());
        Assert.assertEquals("validate description", "Here a long description about the module", module.getDescription());
        Assert.assertEquals("validate scripts", "create db", module.getScript().get(0));
        Assert.assertEquals("validate scripts", 4, module.getScript().size());
    }
}
