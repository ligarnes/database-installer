package net.alteiar.db.installer.xml;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.alteiar.db.installer.exception.DbScriptException;
import net.alteiar.db.installer.xml.util.UnitTestContext;

public class XmlModuleBuilderErrorTest {

  private UnitTestContext unitTestContext;

  @Before
  public void before() {

    unitTestContext = new UnitTestContext();
    unitTestContext.initialize();
  }

  @After
  public void after() {

    unitTestContext.shutdown();
  }

  @Test
  public void testInvalidModulePath() throws DbScriptException {

    try {

      XmlModuleBuilder sqlModule = new XmlModuleBuilder("/xml/invalid/not_found.xml");

      sqlModule.createModule();
    } catch (DbScriptException ex) {

      Assert.assertEquals("The sql module /xml/invalid/not_found.xml is not found", ex.getMessage());
    }
  }

  @Test
  public void testModuleInvalidXml() throws DbScriptException {

    try {
      XmlModuleBuilder sqlModule = new XmlModuleBuilder("/xml/invalid/module_invalid_xml.xml");

      sqlModule.createModule();
      Assert.fail("A DbScriptException is expected");
    } catch (DbScriptException ex) {

      Assert.assertEquals("Failed to parse the sql module /xml/invalid/module_invalid_xml.xml", ex.getMessage());
    }
  }

  @Test
  public void testModuleInvalidLocation() throws DbScriptException {

    try {
      XmlModuleBuilder sqlModule = new XmlModuleBuilder("/xml/invalid/module_invalid_location.xml");

      sqlModule.createModule();
      Assert.fail("A DbScriptException is expected");
    } catch (DbScriptException ex) {

      Assert.assertEquals("The location type class net.alteiar.sql.LocationType is not supported.", ex.getMessage());
    }
  }

  @Test
  public void testModuleInvalidScriptNotFound() throws DbScriptException {

    try {
      XmlModuleBuilder sqlModule = new XmlModuleBuilder("/xml/invalid/module_invalid_script_not_found.xml");

      sqlModule.createModule();
      Assert.fail("A DbScriptException is expected");
    } catch (DbScriptException ex) {

      Assert.assertEquals("The sql script /xml/script/not/found/create db.xml is not found", ex.getMessage());
    }
  }

  @Test
  public void testModuleInvalidScriptInvalid() throws DbScriptException {

    try {
      XmlModuleBuilder sqlModule = new XmlModuleBuilder("/xml/invalid/module_invalid_script_invalid.xml");

      sqlModule.createModule();
      Assert.fail("A DbScriptException is expected");
    } catch (DbScriptException ex) {

      Assert.assertEquals("Failed to parse the sql script /xml/invalid/script_invalid.xml", ex.getMessage());
    }
  }

}
