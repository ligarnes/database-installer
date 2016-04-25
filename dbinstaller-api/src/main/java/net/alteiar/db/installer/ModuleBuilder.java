package net.alteiar.db.installer;

import net.alteiar.db.installer.exception.DbScriptException;

public interface ModuleBuilder {

  /**
   * 
   * @return
   * @throws DbScriptException
   */
  SqlModule createModule() throws DbScriptException;
}
