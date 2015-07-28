package net.alteiar.db.installer.xml;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import net.alteiar.db.installer.exception.ParsingException;
import net.alteiar.sql.ObjectFactory;
import net.alteiar.sql.SqlModule;

import org.slf4j.LoggerFactory;

public class SqlModuleWrapper {

    private SqlModule module;

    public SqlModuleWrapper() {
    }

    public SqlModuleWrapper(SqlModule module) {

        this.module = module;
    }

    public void load(InputStream input) throws ParsingException {

        this.module = JAXBParser.getInstance().unmarshall(input);

    }

    public SqlModule getSqlModule() {

        return this.module;
    }

    @Override
    public String toString() {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ObjectFactory factory = new ObjectFactory();

        try {

            JAXBParser.getInstance().marshall(out, factory.createSqlModule(module));
        } catch (ParsingException e) {

            LoggerFactory.getLogger(getClass()).error("Fail to marshall the SqlModule", e);
        }

        return new String(out.toByteArray());
    }
}
