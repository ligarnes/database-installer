package net.alteiar.db.installer.xml;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;

import net.alteiar.db.installer.exception.ParsingException;
import net.alteiar.sql.ObjectFactory;
import net.alteiar.sql.SqlModuleType;

public class SqlModuleXml {

  private final SqlModuleType module;

  public SqlModuleXml(InputStream input) throws ParsingException {

    this.module = JAXBParser.getInstance().unmarshall(input);
  }

  public SqlModuleXml(SqlModuleType module) {

    this.module = module;
  }

  public SqlModuleType getSqlModule() {

    return this.module;
  }

  @Override
  public String toString() {

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    ObjectFactory factory = new ObjectFactory();

    try {

      JAXBParser.getInstance().marshall(out, factory.createSqlModule(module));
    } catch (ParsingException e) {

      LogManager.getLogger(getClass()).error("Failed to marshall the SqlModule", e);
    }

    return new String(out.toByteArray());
  }
}
