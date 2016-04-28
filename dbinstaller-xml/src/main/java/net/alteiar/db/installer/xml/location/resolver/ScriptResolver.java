package net.alteiar.db.installer.xml.location.resolver;

import net.alteiar.db.installer.SqlScript;
import net.alteiar.db.installer.exception.DbScriptException;
import net.alteiar.sql.LocationType;

public interface ScriptResolver {

  boolean accept(LocationType location);

  SqlScript getScript(LocationType location, String scriptName) throws DbScriptException;

}