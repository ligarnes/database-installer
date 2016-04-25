package net.alteiar.db.installer;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.alteiar.db.installer.dao.DbInstallerDao;
import net.alteiar.db.installer.exception.DbScriptException;

public class SqlModuleImpl implements SqlModule {

  private static final Logger LOGGER = LogManager.getLogger(SqlModuleImpl.class);

  private final List<SqlScript> scripts;

  private final String name;

  public SqlModuleImpl(String moduleName) {

    this.name = moduleName;
    this.scripts = new ArrayList<>();
  }

  @Override
  public String getName() {

    return name;
  }

  public void addScript(SqlScript script) {

    this.scripts.add(script);
  }

  @Override
  public void install() throws DbScriptException {

    DbInstallerDao dbInstallerDao = DbInstallerContext.getInstance().getDbInstallerDao();

    if (dbInstallerDao.createTable()) {

      LOGGER.debug("Create [db_installer] table");
    } else {

      LOGGER.debug("The [db_installer] table is already created");
    }

    LOGGER.debug("Execute scripts in module [{}]", getName());

    // execute all scripts of this module
    for (SqlScript script : scripts) {

      String scriptName = script.getName();

      // verify if script is already applied
      if (!dbInstallerDao.findScript(getName(), scriptName)) {

        try {

          // execute the script
          LOGGER.debug("Execute script [{}] in module [{}]", scriptName, getName());
          dbInstallerDao.executeScript(script.getQuery());
          dbInstallerDao.insertScript(getName(), scriptName, script.getDescription());
          LOGGER.debug("Script [{}] in module [{}] executed with success", scriptName, getName());

        } catch (RuntimeException ex) {

          // failure in script execution
          throw new DbScriptException(String.format("Failure to apply script [%s] in module [%s]", scriptName,
                                                    getName()),
                                      ex);
        }

      } else {

        // script already executed
        LOGGER.debug("The script '{}' in module '{}' is already applied", scriptName, getName());
      }
    }
  }

}
