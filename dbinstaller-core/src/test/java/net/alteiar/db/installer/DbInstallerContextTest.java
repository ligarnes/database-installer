package net.alteiar.db.installer;

import org.junit.Before;
import org.junit.Test;

import net.alteiar.db.installer.utils.UnitTestContext;

public class DbInstallerContextTest {

  @Before
  public void before() {

    UnitTestContext context = new UnitTestContext();
    context.shutdown();
  }

  @Test(expected = IllegalStateException.class)
  public void testUninitializedContext() {

    DbInstallerContext.getInstance().getDbInstallerDao();
  }
}
