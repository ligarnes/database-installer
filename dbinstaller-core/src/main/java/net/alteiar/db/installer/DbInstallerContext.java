package net.alteiar.db.installer;

import javax.sql.DataSource;

import net.alteiar.db.installer.dao.DbInstallerDao;

public class DbInstallerContext {

  private static final DbInstallerContext instance = new DbInstallerContext();

  public static final DbInstallerContext getInstance() {

    return instance;
  }

  private DbInstallerDao dao;

  private DbInstallerContext() {
  }

  public void initialize(DataSource ds) {

    dao = new DbInstallerDao();
    dao.setDataSource(ds);
  }

  public DbInstallerDao getDbInstallerDao() {

    if (dao == null) {

      throw new IllegalStateException("The DbInstallerContext must be initialized");
    }
    return dao;
  }
}
