package net.alteiar.db.installer.xml.location.resolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import net.alteiar.db.installer.SqlScript;
import net.alteiar.db.installer.exception.DbScriptException;
import net.alteiar.db.installer.xml.SqlScriptXml;
import net.alteiar.db.installer.xml.exception.ParsingException;
import net.alteiar.sql.FileLocationType;
import net.alteiar.sql.LocationType;

public class FileResolver implements ScriptResolver {

  @Override
  public boolean accept(LocationType location) {

    return location instanceof FileLocationType;
  }

  @Override
  public SqlScript getScript(LocationType location, String scriptName) throws DbScriptException {

    FileLocationType fileLocation = (FileLocationType) location;

    File folder = new File(fileLocation.getFolder());

    File script = new File(folder, scriptName + ".xml");

    SqlScriptXml sqlScriptWrapper;

    try {
      InputStream sqlScriptStream = new FileInputStream(script);

      sqlScriptWrapper = new SqlScriptXml(sqlScriptStream);
    } catch (FileNotFoundException e) {

      throw new DbScriptException(String.format("The sql script %s is not found", script.getPath()), e);
    } catch (ParsingException e) {

      throw new DbScriptException(String.format("Failed to parse the sql script %s", script.getPath()), e);
    }

    return sqlScriptWrapper;
  }
}
