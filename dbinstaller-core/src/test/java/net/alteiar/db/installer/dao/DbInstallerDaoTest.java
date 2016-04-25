package net.alteiar.db.installer.dao;

import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.alteiar.db.installer.utils.UnitTestContext;

public class DbInstallerDaoTest {

	private static final long SECOND_IN_MS = 1000L;
	private DbInstallerDao dao;

	private UnitTestContext context;

	@Before
	public void before() throws Exception {

		context = new UnitTestContext();
		context.initialize();

		dao = new DbInstallerDao();
		dao.setDataSource(context.getDatasource());

		dao.dropTable();
		context.deleteDatabase();
	}

	@After
	public void after() {

		dao.dropTable();
		context.deleteDatabase();
	}

	@Test
	public void testDao() {

		final String moduleName = "module";
		final String scriptName = "name";

		// verify table does not exist
		try {

			dao.findScript(moduleName, scriptName);
			Assert.fail("Find should fail, table is not created");
		} catch (RuntimeException ex) {

		}

		dao.createTable();

		Assert.assertFalse(dao.findScript(moduleName, scriptName));

		Date currentTime = new Date();

		dao.insertScript(moduleName, scriptName, "description");

		Date insertedDate = context.getJdbcTemplate().queryForObject("SELECT insert_time FROM db_installer",
				Date.class);

		Assert.assertTrue(currentTime.getTime() <= insertedDate.getTime());
		Assert.assertTrue(currentTime.getTime() + SECOND_IN_MS > insertedDate.getTime());

		Assert.assertTrue(dao.findScript(moduleName, scriptName));
		Assert.assertFalse(dao.findScript("invalid", scriptName));
		Assert.assertFalse(dao.findScript(moduleName, "invalid"));

		dao.dropTable();

		// verify table is dropped
		try {

			dao.findScript(moduleName, scriptName);
			Assert.fail("Find should fail, table should be dropped");
		} catch (RuntimeException ex) {

		}
	}

}
