package net.alteiar.db.installer.utils;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import net.alteiar.db.installer.DbInstallerContext;

public class UnitTestContext {

  private DataSource ds;

  private JdbcTemplate jdbcTemplate;

  public void initialize() throws ClassNotFoundException {

    Class.forName("org.h2.Driver");

    JdbcDataSource jdbcDatasource = new JdbcDataSource();
    jdbcDatasource.setURL("jdbc:h2:./test");
    jdbcDatasource.setUser("sa");
    jdbcDatasource.setPassword("sa");

    this.ds = jdbcDatasource;

    DbInstallerContext.getInstance().initialize(ds);
    jdbcTemplate = new JdbcTemplate(ds);
  }

  public void shutdown() {

    ReflectUtils.setField(DbInstallerContext.getInstance(), "dao", null);
  }

  public DataSource getDatasource() {

    return ds;
  }

  public JdbcTemplate getJdbcTemplate() {

    return jdbcTemplate;
  }

  public void deleteDatabase() {

    jdbcTemplate.execute("DROP ALL OBJECTS DELETE FILES");
  }
}
