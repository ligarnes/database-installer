package net.alteiar.db.installer.xml.location.resolver;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import net.alteiar.db.installer.SqlScript;
import net.alteiar.db.installer.exception.DbScriptException;
import net.alteiar.sql.FileLocationType;
import net.alteiar.sql.JarLocationType;
import net.alteiar.sql.LocationType;

public class FileResolverTest {

  @Test
  public void testAccept() {

    FileResolver resolver = new FileResolver();

    Assert.assertFalse(resolver.accept(new LocationType()));
    Assert.assertFalse(resolver.accept(new JarLocationType()));
    Assert.assertTrue(resolver.accept(new FileLocationType()));
  }

  @Test
  public void testGetScriptNotFound() {

    FileResolver resolver = new FileResolver();

    String folder = "./src/test/resources/xml/not/found";
    FileLocationType locationType = new FileLocationType();
    locationType.setFolder(folder);

    try {

      resolver.getScript(locationType, "any");
      Assert.fail("A DbScriptException is expected");
    } catch (DbScriptException ex) {

      File file = new File(folder, "any.xml");
      Assert.assertEquals("The sql script " + file.getPath() + " is not found", ex.getMessage());
    }
  }

  @Test
  public void testGetScriptInvalid() {

    FileResolver resolver = new FileResolver();

    String folder = "./src/test/resources/xml/invalid";

    FileLocationType locationType = new FileLocationType();
    locationType.setFolder(folder);

    try {

      resolver.getScript(locationType, "script_invalid");
      Assert.fail("A DbScriptException is expected");
    } catch (DbScriptException ex) {

      File file = new File(folder, "script_invalid.xml");

      Assert.assertEquals("Failed to parse the sql script " + file.getPath(), ex.getMessage());
    }
  }

  @Test
  public void testGetScript() throws DbScriptException {

    FileResolver resolver = new FileResolver();

    FileLocationType locationType = new FileLocationType();
    locationType.setFolder("./src/test/resources/xml/script");

    SqlScript sqlScript = resolver.getScript(locationType, "create db");

    Assert.assertEquals("create db", sqlScript.getName());
    Assert.assertEquals("Here a description about the query", sqlScript.getDescription());
    Assert.assertEquals("CREATE TABLE test (id int(10), name varchar(120), description varchar(250))",
                        sqlScript.getQuery());
  }
}
