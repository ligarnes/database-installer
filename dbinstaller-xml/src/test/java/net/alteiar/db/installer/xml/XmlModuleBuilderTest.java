package net.alteiar.db.installer.xml;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.alteiar.db.installer.SqlModule;
import net.alteiar.db.installer.exception.DbScriptException;
import net.alteiar.db.installer.xml.util.UnitTestContext;

public class XmlModuleBuilderTest {

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
  public void testJarScript() throws DbScriptException {

    XmlModuleBuilder sqlModule = new XmlModuleBuilder("/xml/module.xml");

    SqlModule module = sqlModule.createModule();
    module.install();
  }
}
