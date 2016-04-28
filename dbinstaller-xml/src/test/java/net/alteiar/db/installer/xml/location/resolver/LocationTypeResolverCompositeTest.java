package net.alteiar.db.installer.xml.location.resolver;

import org.junit.Assert;
import org.junit.Test;

import net.alteiar.db.installer.SqlScript;
import net.alteiar.db.installer.exception.DbScriptException;
import net.alteiar.sql.FileLocationType;
import net.alteiar.sql.JarLocationType;
import net.alteiar.sql.LocationType;

public class LocationTypeResolverCompositeTest {

  @Test
  public void testAccept() {

    LocationTypeResolverComposite resolver = new LocationTypeResolverComposite();

    Assert.assertFalse(resolver.accept(new LocationType()));
    Assert.assertFalse(resolver.accept(new JarLocationType()));
    Assert.assertFalse(resolver.accept(new FileLocationType()));

    resolver = new LocationTypeResolverComposite();
    resolver.addResolver(new JarResolver());
    Assert.assertFalse(resolver.accept(new LocationType()));
    Assert.assertTrue(resolver.accept(new JarLocationType()));
    Assert.assertFalse(resolver.accept(new FileLocationType()));

    resolver = new LocationTypeResolverComposite();
    resolver.addResolver(new FileResolver());
    Assert.assertFalse(resolver.accept(new LocationType()));
    Assert.assertFalse(resolver.accept(new JarLocationType()));
    Assert.assertTrue(resolver.accept(new FileLocationType()));

    resolver = new LocationTypeResolverComposite();
    resolver.addResolver(new FileResolver());
    resolver.addResolver(new JarResolver());
    Assert.assertFalse(resolver.accept(new LocationType()));
    Assert.assertTrue(resolver.accept(new JarLocationType()));
    Assert.assertTrue(resolver.accept(new FileLocationType()));
  }

  @Test
  public void testNoResolver() throws DbScriptException {

    LocationTypeResolverComposite resolver = new LocationTypeResolverComposite();

    FileLocationType locationType = new FileLocationType();
    locationType.setFolder("./src/test/resources/xml/script");

    try {

      resolver.getScript(locationType, "create db");
      Assert.fail("A DbScriptException is expected");
    } catch (IllegalArgumentException ex) {
      Assert.assertEquals(String.format("The location type %s is not supported.%n"
                                        + "You should use accept before to ensure that the resolver support the provided type",
                                        FileLocationType.class.getName()),
                          ex.getMessage());
    }
  }

  @Test
  public void testGetScript() throws DbScriptException {

    LocationTypeResolverComposite resolver = new LocationTypeResolverComposite();
    resolver.addResolver(new FileResolver());

    FileLocationType locationType = new FileLocationType();
    locationType.setFolder("./src/test/resources/xml/script");

    SqlScript sqlScript = resolver.getScript(locationType, "create db");

    Assert.assertEquals("create db", sqlScript.getName());
    Assert.assertEquals("Here a description about the query", sqlScript.getDescription());
    Assert.assertEquals("CREATE TABLE test (id int(10), name varchar(120), description varchar(250))",
                        sqlScript.getQuery());
  }
}
