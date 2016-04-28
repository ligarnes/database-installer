package net.alteiar.db.installer.xml.util;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import net.alteiar.db.installer.DbInstallerContext;

public class UnitTestContext {

  private DataSource ds;

  private JdbcTemplate jdbcTemplate;

  public void initialize() {

    try {
      Class.forName("org.h2.Driver");
    } catch (ClassNotFoundException e) {

      throw new RuntimeException("Unexpected excetion, failed to load h2 driver", e);
    }

    JdbcDataSource jdbcDatasource = new JdbcDataSource();
    // jdbcDatasource.setURL("jdbc:h2:./test");
    jdbcDatasource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    jdbcDatasource.setUser("sa");
    jdbcDatasource.setPassword("sa");

    this.ds = jdbcDatasource;

    DbInstallerContext.getInstance().initialize(ds);
    jdbcTemplate = new JdbcTemplate(ds);
  }

  public void shutdown() {

    ReflectUtils.setField(DbInstallerContext.getInstance(), "dao", null);
    deleteDatabase();
  }

  public DataSource getDatasource() {

    return ds;
  }

  public JdbcTemplate getJdbcTemplate() {

    return jdbcTemplate;
  }

  private void deleteDatabase() {

    jdbcTemplate.execute("DROP ALL OBJECTS DELETE FILES");
  }
}
