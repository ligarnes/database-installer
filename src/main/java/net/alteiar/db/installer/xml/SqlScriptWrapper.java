package net.alteiar.db.installer.xml;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import net.alteiar.db.installer.exception.ParsingException;
import net.alteiar.sql.ObjectFactory;
import net.alteiar.sql.SqlScript;

import org.slf4j.LoggerFactory;

public class SqlScriptWrapper {

    private SqlScript sqlScript;

    public SqlScriptWrapper() {

    }

    public SqlScriptWrapper(SqlScript sqlScript) {

        this.sqlScript = sqlScript;
    }

    public void load(InputStream input) throws ParsingException {

        this.sqlScript = JAXBParser.getInstance().unmarshall(input);

    }

    public SqlScript getSqlScript() {

        return this.sqlScript;
    }

    @Override
    public String toString() {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ObjectFactory factory = new ObjectFactory();

        try {

            JAXBParser.getInstance().marshall(out, factory.createSqlScript(sqlScript));
        } catch (ParsingException e) {

            LoggerFactory.getLogger(getClass()).error("Fail to marshall the sqlScript", e);
        }

        return new String(out.toByteArray());
    }
}
