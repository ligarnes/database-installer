package net.alteiar.db.installer.xml.location.resolver;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;

import net.alteiar.db.installer.SqlScript;
import net.alteiar.db.installer.exception.DbScriptException;
import net.alteiar.db.installer.xml.SqlScriptXml;
import net.alteiar.db.installer.xml.exception.ParsingException;
import net.alteiar.sql.JarLocationType;
import net.alteiar.sql.LocationType;

public class JarResolver implements ScriptResolver {

  @Override
  public boolean accept(LocationType location) {

    return location instanceof JarLocationType;
  }

  @Override
  public SqlScript getScript(LocationType location, String scriptName) throws DbScriptException {

    JarLocationType jarLocation = (JarLocationType) location;

    String scriptCompleteName = "/" + jarLocation.getClasspath().replaceAll("\\.", "/") + "/" + scriptName + ".xml";

    LogManager.getLogger(getClass()).debug("Load class: " + scriptCompleteName);
    InputStream sqlScriptStream = getClass().getResourceAsStream(scriptCompleteName);

    if (sqlScriptStream == null) {

      throw new DbScriptException(String.format("The sql script %s is not found", scriptCompleteName));
    }

    SqlScriptXml sqlScriptWrapper;
    try {

      sqlScriptWrapper = new SqlScriptXml(sqlScriptStream);
    } catch (ParsingException e) {

      throw new DbScriptException(String.format("Failed to parse the sql script %s", scriptCompleteName), e);
    }

    return sqlScriptWrapper;
  }
}
