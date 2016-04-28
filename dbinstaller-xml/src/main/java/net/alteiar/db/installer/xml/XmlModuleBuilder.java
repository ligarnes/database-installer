package net.alteiar.db.installer.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.alteiar.db.installer.ModuleBuilder;
import net.alteiar.db.installer.SqlModule;
import net.alteiar.db.installer.SqlModuleImpl;
import net.alteiar.db.installer.exception.DbScriptException;
import net.alteiar.db.installer.xml.exception.ParsingException;
import net.alteiar.db.installer.xml.location.resolver.FileResolver;
import net.alteiar.db.installer.xml.location.resolver.JarResolver;
import net.alteiar.db.installer.xml.location.resolver.ScriptResolver;
import net.alteiar.db.installer.xml.location.resolver.LocationTypeResolverComposite;
import net.alteiar.sql.LocationType;

public class XmlModuleBuilder implements ModuleBuilder {

  private static final Logger logger = LogManager.getLogger(XmlModuleBuilder.class);

  private final String module;

  private final ScriptResolver resolver;

  public XmlModuleBuilder(String moduleFile) {

    this.module = moduleFile;

    LocationTypeResolverComposite resolver = new LocationTypeResolverComposite();
    resolver.addResolver(new JarResolver());
    resolver.addResolver(new FileResolver());

    this.resolver = resolver;
  }

  @Override
  public SqlModule createModule() throws DbScriptException {

    SqlModuleXml moduleWrapper = null;

    InputStream moduleXml = getClass().getResourceAsStream(module);

    if (moduleXml == null) {

      throw new DbScriptException(String.format("The sql module %s is not found", module));
    }

    try {

      moduleWrapper = new SqlModuleXml(moduleXml);
    } catch (ParsingException e) {

      throw new DbScriptException(String.format("Failed to parse the sql module %s", module), e);
    } finally {

      try {

        moduleXml.close();
      } catch (IOException e) {

        logger.warn("Failed to close the module inputStream", e);
      }
    }

    String moduleName = moduleWrapper.getSqlModule().getName();

    LocationType location = moduleWrapper.getSqlModule().getLocation();

    if (!resolver.accept(location)) {

      throw new DbScriptException(String.format("The location type %s is not supported.", location.getClass()));
    }

    List<String> scripts = moduleWrapper.getSqlModule().getScript();

    SqlModuleImpl moduleImpl = new SqlModuleImpl(moduleName);

    for (String scriptName : scripts) {

      LogManager.getLogger(getClass()).debug("Load file");
      moduleImpl.addScript(resolver.getScript(location, scriptName));
    }

    return moduleImpl;
  }

}
