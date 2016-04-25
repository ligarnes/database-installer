package net.alteiar.db.installer.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import net.alteiar.db.installer.ModuleBuilder;
import net.alteiar.db.installer.SqlModule;
import net.alteiar.db.installer.SqlModuleImpl;
import net.alteiar.db.installer.SqlScript;
import net.alteiar.db.installer.exception.DbScriptException;
import net.alteiar.db.installer.exception.ParsingException;
import net.alteiar.db.installer.xml.SqlModuleXml;
import net.alteiar.db.installer.xml.SqlScriptXml;
import net.alteiar.sql.FileLocationType;
import net.alteiar.sql.JarLocationType;
import net.alteiar.sql.LocationType;

public class XmlModuleBuilder implements ModuleBuilder {

  private final String module;

  public XmlModuleBuilder(String moduleFile) {

    this.module = moduleFile;
  }

  private SqlScript getJarScript(JarLocationType location, String scriptName) throws DbScriptException {

    String scriptCompleteName = location.getClasspath().replaceAll("\\.", "/") + "/" + scriptName + ".xml";

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

  private SqlScript getFileScript(FileLocationType location, String scriptName) throws DbScriptException {

    File folder = new File(location.getFolder());

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

  private SqlScript getScript(LocationType location, String scriptName) throws DbScriptException {

    SqlScript script = null;

    if (script instanceof JarLocationType) {

      script = getJarScript((JarLocationType) location, scriptName);
    } else if (script instanceof FileLocationType) {

      script = getFileScript((FileLocationType) location, scriptName);
    }

    return script;
  }

  @Override
  public SqlModule createModule() throws DbScriptException {

    SqlModuleXml moduleWrapper = null;

    InputStream moduleXml = getClass().getResourceAsStream(module);

    if (moduleXml == null) {

      throw new DbScriptException(String.format("The sql module %s is not found", moduleXml));
    }

    try {

      moduleWrapper = new SqlModuleXml(moduleXml);
    } catch (ParsingException e) {

      throw new DbScriptException(String.format("Failed to parse the sql module %s", moduleXml), e);
    }

    String moduleName = moduleWrapper.getSqlModule().getName();

    LocationType location = moduleWrapper.getSqlModule().getLocation();

    List<String> scripts = moduleWrapper.getSqlModule().getScript();

    SqlModuleImpl moduleImpl = new SqlModuleImpl(moduleName);

    for (String scriptName : scripts) {

      moduleImpl.addScript(getScript(location, scriptName));
    }

    return moduleImpl;
  }

}
