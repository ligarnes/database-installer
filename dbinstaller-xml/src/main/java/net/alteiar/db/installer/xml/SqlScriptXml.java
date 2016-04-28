package net.alteiar.db.installer.xml;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;

import net.alteiar.db.installer.xml.exception.ParsingException;
import net.alteiar.sql.ObjectFactory;
import net.alteiar.sql.SqlScriptType;

public class SqlScriptXml implements net.alteiar.db.installer.SqlScript {

  private final SqlScriptType sqlScript;

  public SqlScriptXml(InputStream input) throws ParsingException {

    this.sqlScript = JAXBParser.getInstance().unmarshall(input);
  }

  public SqlScriptXml(SqlScriptType sqlScript) {

    this.sqlScript = sqlScript;
  }

  @Override
  public String getName() {

    return sqlScript.getName();
  }

  @Override
  public String getDescription() {

    return sqlScript.getDescription();
  }

  @Override
  public String getQuery() {

    return sqlScript.getQuery();
  }

  @Override
  public String toString() {

    String xml = null;
    ObjectFactory factory = new ObjectFactory();

    try {

      xml = JAXBParser.getInstance().marshall(factory.createSqlScript(sqlScript));
    } catch (ParsingException e) {

      LogManager.getLogger(getClass()).error("Failed to marshall the sqlScript", e);
    }

    return xml;
  }

}
