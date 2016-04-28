package net.alteiar.db.installer.xml.location.resolver;

import org.junit.Assert;
import org.junit.Test;

import net.alteiar.db.installer.SqlScript;
import net.alteiar.db.installer.exception.DbScriptException;
import net.alteiar.sql.FileLocationType;
import net.alteiar.sql.JarLocationType;
import net.alteiar.sql.LocationType;

public class JarResolverTest {

  @Test
  public void testAccept() {

    JarResolver resolver = new JarResolver();

    Assert.assertFalse(resolver.accept(new LocationType()));
    Assert.assertFalse(resolver.accept(new FileLocationType()));
    Assert.assertTrue(resolver.accept(new JarLocationType()));
  }

  @Test
  public void testGetScriptNotFound() {

    JarResolver resolver = new JarResolver();

    JarLocationType locationType = new JarLocationType();
    locationType.setClasspath("xml.not.found");

    try {

      resolver.getScript(locationType, "any");
      Assert.fail("A DbScriptException is expected");
    } catch (DbScriptException ex) {

      Assert.assertEquals("The sql script /xml/not/found/any.xml is not found", ex.getMessage());
    }
  }

  @Test
  public void testGetScriptInvalid() {

    JarResolver resolver = new JarResolver();

    JarLocationType locationType = new JarLocationType();
    locationType.setClasspath("xml.invalid");

    try {

      resolver.getScript(locationType, "script_invalid");
      Assert.fail("A DbScriptException is expected");
    } catch (DbScriptException ex) {

      Assert.assertEquals("Failed to parse the sql script /xml/invalid/script_invalid.xml", ex.getMessage());
    }
  }

  @Test
  public void testGetScript() throws DbScriptException {

    JarResolver resolver = new JarResolver();

    JarLocationType locationType = new JarLocationType();
    locationType.setClasspath("xml.script");

    SqlScript sqlScript = resolver.getScript(locationType, "create db");

    Assert.assertEquals("create db", sqlScript.getName());
    Assert.assertEquals("Here a description about the query", sqlScript.getDescription());
    Assert.assertEquals("CREATE TABLE test (id int(10), name varchar(120), description varchar(250))",
                        sqlScript.getQuery());
  }
}
