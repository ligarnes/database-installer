package net.alteiar.db.installer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.alteiar.db.installer.exception.DbScriptException;
import net.alteiar.sql.SqlModule;
import net.alteiar.sql.SqlScript;

public abstract class ModuleImpl<E extends SqlModule> implements IModule {

    private final E sqlModule;

    private final Map<String, SqlScript> scripts;

    public ModuleImpl(E sqlModule) {
        super();
        this.sqlModule = sqlModule;

        this.scripts = new HashMap<String, SqlScript>();
    }

    @Override
    public String getName() {

        return this.sqlModule.getName();
    }

    protected abstract SqlScript loadScript(String name) throws Exception;

    public final void loadScripts() throws DbScriptException {

        for (String scriptName : sqlModule.getScript()) {

            try {
                SqlScript loaded = loadScript(scriptName);

                scripts.put(loaded.getName(), loaded);
            } catch (Exception e) {

                throw new DbScriptException(String.format("Fail to load the script %s", scriptName), e);
            }
        }
    }

    @Override
    public SqlScript getScript(String name) {
        return scripts.get(name);
    }

    public List<String> getScriptNameList() {

        return sqlModule.getScript();
    }

    protected E getSqlModule() {
        return sqlModule;
    }

    @Override
    public Iterator<SqlScript> iterator() {

        return new SqlScriptIterator(this);
    }
}
