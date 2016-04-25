package net.alteiar.db.installer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.alteiar.db.installer.exception.DbScriptException;
import net.alteiar.db.installer.utils.SimpleSqlScript;
import net.alteiar.db.installer.utils.UnitTestContext;

public class SqlModuleImplTest {

  private UnitTestContext context;

  @Before
  public void before() throws Exception {

    context = new UnitTestContext();
    context.initialize();
    context.deleteDatabase();
  }

  @After
  public void after() {

    context.deleteDatabase();
    context.shutdown();
  }

  private SqlScript createScript1() {

    SimpleSqlScript sqlScript1 = new SimpleSqlScript();
    sqlScript1.setName("create db");
    sqlScript1.setDescription("Here a description about the query");
    sqlScript1.setQuery("CREATE TABLE whois (id int(10), name varchar(120), description varchar(250))");

    return sqlScript1;
  }

  private SqlScript createScript2() {

    SimpleSqlScript sqlScript1 = new SimpleSqlScript();
    sqlScript1.setName("insert value");
    sqlScript1.setDescription("Insert the value cody");
    sqlScript1.setQuery("INSERT INTO whois VALUES (1, 'cody', 'dev')");

    return sqlScript1;
  }

  private SqlScript createScript3() {

    SimpleSqlScript sqlScript1 = new SimpleSqlScript();
    sqlScript1.setName("insert value 2");
    sqlScript1.setDescription("Insert the value aervia");
    sqlScript1.setQuery("INSERT INTO whois VALUES (2, 'aervia', 'joy')");

    return sqlScript1;
  }

  private SqlScript createScript4() {

    SimpleSqlScript sqlScript1 = new SimpleSqlScript();
    sqlScript1.setName("insert value 3");
    sqlScript1.setDescription("Insert the value cody");
    sqlScript1.setQuery("INSERT INTO whois VALUES (3, 'hugo', 'friend')");

    return sqlScript1;
  }

  @Test
  public void install() throws DbScriptException {

    SqlModuleImpl moduleInstaller = new SqlModuleImpl("MyTestModule");
    moduleInstaller.addScript(createScript1());
    moduleInstaller.addScript(createScript2());
    moduleInstaller.addScript(createScript3());
    moduleInstaller.addScript(createScript4());

    moduleInstaller.install();

    // make a select
    int count = context.getJdbcTemplate().queryForObject("select count(*) from whois", Integer.class);

    Assert.assertEquals(3, count);

    count = context.getJdbcTemplate().queryForObject("select count(*) from whois where name = 'aervia'", Integer.class);
    Assert.assertEquals(1, count);
  }

  @Test
  public void update() throws DbScriptException {

    SqlModuleImpl moduleInstaller = new SqlModuleImpl("MyTestModule");
    moduleInstaller.addScript(createScript1());
    moduleInstaller.addScript(createScript2());

    moduleInstaller.install();

    // verify installation
    int count = context.getJdbcTemplate().queryForObject("select count(*) from whois", Integer.class);

    Assert.assertEquals(1, count);

    count = context.getJdbcTemplate().queryForObject("select count(*) from whois where name = 'aervia'", Integer.class);
    Assert.assertEquals(0, count);

    // create the same sqlModule + some new script
    // Only new script should be applyied
    moduleInstaller = new SqlModuleImpl("MyTestModule");
    moduleInstaller.addScript(createScript1());
    moduleInstaller.addScript(createScript2());
    moduleInstaller.addScript(createScript3());
    moduleInstaller.addScript(createScript4());

    moduleInstaller.install();

    count = context.getJdbcTemplate().queryForObject("select count(*) from whois", Integer.class);

    Assert.assertEquals(3, count);

    count = context.getJdbcTemplate().queryForObject("select count(*) from whois where name = 'aervia'", Integer.class);
    Assert.assertEquals(1, count);
  }

  @Test(expected = DbScriptException.class)
  public void executeWithException() throws DbScriptException {

    SqlModuleImpl moduleInstaller = new SqlModuleImpl("MyTestModule");
    moduleInstaller.addScript(createScript2());
    moduleInstaller.addScript(createScript1());

    moduleInstaller.install();
  }
}
