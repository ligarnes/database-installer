package net.alteiar.db.installer.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.alteiar.db.installer.ModuleImpl;
import net.alteiar.db.installer.exception.ParsingException;
import net.alteiar.db.installer.xml.SqlScriptWrapper;
import net.alteiar.sql.SqlFileModule;
import net.alteiar.sql.SqlScript;

public class FileModule extends ModuleImpl<SqlFileModule> {

    public FileModule(SqlFileModule sqlModule) {
        super(sqlModule);
    }

    @Override
    protected SqlScript loadScript(String name) throws ParsingException, FileNotFoundException {

        File scriptFile = new File(getSqlModule().getDir() + File.separatorChar + name + ".xml");

        SqlScriptWrapper wrapper = new SqlScriptWrapper();

        wrapper.load(new FileInputStream(scriptFile));

        return wrapper.getSqlScript();
    }

}
