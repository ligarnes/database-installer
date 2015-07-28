package net.alteiar.db.installer.dao;

import java.util.Date;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class DbInstallerDaoTest {

    private static final long SECOND_IN_MS = 1000L;
    private JdbcDataSource ds;
    private DbInstallerDao dao;

    @Before
    public void before() throws Exception {

        Class.forName("org.h2.Driver");

        ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:./test");
        ds.setUser("sa");
        ds.setPassword("sa");

        dao = new DbInstallerDao();
        dao.setDataSource(ds);

        try {

            dao.dropTable();
        } catch (Exception ex) {

        }
    }

    @After
    public void after() {

        try {

            dao.dropTable();
        } catch (Exception ex) {

        }
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

        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        Date insertedDate = jdbcTemplate.queryForObject("SELECT insert_time FROM db_installer", Date.class);

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
