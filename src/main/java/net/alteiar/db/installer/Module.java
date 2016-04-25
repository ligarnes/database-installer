package net.alteiar.db.installer;

import net.alteiar.db.installer.exception.DbScriptException;
import net.alteiar.sql.SqlScript;

public interface IModule extends Iterable<SqlScript> {

    /**
     *
     * @return the module name
     */
    String getName();

    /**
     * retrieve the sql script with the specified name
     *
     * @param name
     *            name of the script
     * @return the sql script
     */
    SqlScript getScript(String name);

    /**
     *
     * @throws DbScriptException
     */
    void loadScripts() throws DbScriptException;
}
