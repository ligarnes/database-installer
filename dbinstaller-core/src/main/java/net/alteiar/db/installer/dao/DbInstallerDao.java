package net.alteiar.db.installer.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class DbInstallerDao {

  private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS db_installer (module varchar(120), name varchar(120), description varchar(250), insert_time datetime)";

  private static final String DROP_TABLE = "DROP TABLE db_installer IF EXISTS";

  private static final String INSERT = "INSERT INTO db_installer VALUES (?, ?, ?, ?)";

  private static final String SELECT = "SELECT count(module) FROM db_installer WHERE module = ? AND name = ?";

  private JdbcTemplate jdbcTemplate;

  public void setDataSource(DataSource ds) {

    this.jdbcTemplate = new JdbcTemplate(ds);
  }

  public boolean createTable() {

    return jdbcTemplate.update(CREATE_TABLE) == 1;
  }

  public void dropTable() {

    jdbcTemplate.execute(DROP_TABLE);
  }

  public void insertScript(String module, String name, String description) {

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("module", module);
    parameters.put("name", name);
    parameters.put("description", description);

    jdbcTemplate.update(INSERT, module, name, description, new Date());
  }

  public boolean findScript(String module, String scriptName) {

    int found = jdbcTemplate.queryForObject(SELECT, new Object[] { module, scriptName }, Integer.class);

    return found > 0;
  }

  public void executeScript(String sqlScript) {

    jdbcTemplate.execute(sqlScript);
  }

}
