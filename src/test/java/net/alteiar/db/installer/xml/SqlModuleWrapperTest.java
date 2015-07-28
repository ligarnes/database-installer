package net.alteiar.db.installer.xml;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import net.alteiar.db.installer.exception.ParsingException;
import net.alteiar.sql.SqlModule;

import org.junit.Assert;
import org.junit.Test;

public class SqlModuleWrapperTest {

    @Test(expected = ParsingException.class)
    public void testInvalid() throws Exception {

        SqlModuleWrapper wrapper = new SqlModuleWrapper();

        wrapper.load(getClass().getResourceAsStream("/xml/invalid/module_invalid.xml"));
    }

    @Test
    public void testValid() throws Exception {

        SqlModuleWrapper wrapper = new SqlModuleWrapper();

        wrapper.load(getClass().getResourceAsStream("/xml/module.xml"));

        SqlModule module = wrapper.getSqlModule();
        Assert.assertEquals("validate name", "test", module.getName());
        Assert.assertEquals("validate description", "Here a long description about the module", module.getDescription());
        Assert.assertEquals("validate scripts", "create db", module.getScript().get(0));
        Assert.assertEquals("validate scripts", 4, module.getScript().size());

        wrapper = new SqlModuleWrapper(module);

        module = wrapper.getSqlModule();
        Assert.assertEquals("validate name", "test", module.getName());
        Assert.assertEquals("validate description", "Here a long description about the module", module.getDescription());
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

        SqlModuleWrapper wrapper = new SqlModuleWrapper();

        wrapper.load(getClass().getResourceAsStream("/xml/module.xml"));

        SqlModule module = wrapper.getSqlModule();
        Assert.assertEquals("validate name", "test", module.getName());
        Assert.assertEquals("validate description", "Here a long description about the module", module.getDescription());
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
