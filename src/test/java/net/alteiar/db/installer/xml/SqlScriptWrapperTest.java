package net.alteiar.db.installer.xml;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import net.alteiar.db.installer.exception.ParsingException;
import net.alteiar.sql.SqlScript;

import org.junit.Assert;
import org.junit.Test;

public class SqlScriptWrapperTest {

    @Test(expected = ParsingException.class)
    public void testInvalid() throws Exception {

        new SqlScriptWrapper().load(getClass().getResourceAsStream("/xml/invalid/script_invalid.xml"));
    }

    @Test
    public void testValid() throws Exception {

        SqlScriptWrapper wrapper = new SqlScriptWrapper();

        wrapper.load(getClass().getResourceAsStream("/xml/script.xml"));

        SqlScript script = wrapper.getSqlScript();
        Assert.assertEquals("validate name", "create db", script.getName());
        Assert.assertEquals("validate description", "Here a description about the query", script.getDescription());
        Assert.assertEquals("validate query",
                "CREATE TABLE test (id int(10), name varchar(120), description varchar(250))", script.getQuery());

        script.setName("create db 2");

        wrapper = new SqlScriptWrapper(script);

        script = wrapper.getSqlScript();
        Assert.assertEquals("validate name", "create db 2", script.getName());
        Assert.assertEquals("validate description", "Here a description about the query", script.getDescription());
        Assert.assertEquals("validate query",
                "CREATE TABLE test (id int(10), name varchar(120), description varchar(250))", script.getQuery());
    }

    @Test
    public void testValidRead() throws Exception {

        SqlScriptWrapper wrapper = new SqlScriptWrapper();

        wrapper.load(getClass().getResourceAsStream("/xml/script.xml"));

        SqlScript module = wrapper.getSqlScript();
        Assert.assertEquals("validate name", "create db", module.getName());
        Assert.assertEquals("validate description", "Here a description about the query", module.getDescription());
        Assert.assertEquals("validate query",
                "CREATE TABLE test (id int(10), name varchar(120), description varchar(250))", module.getQuery());

        List<String> lines = Files.readAllLines(Paths.get(getClass().getResource("/xml/script.xml").toURI()));

        StringBuilder file = new StringBuilder();

        for (String line : lines) {
            file.append(line.trim());
        }

        Assert.assertEquals(file.toString(), wrapper.toString());
    }
}
