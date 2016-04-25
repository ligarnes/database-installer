package net.alteiar.db.installer;

import net.alteiar.db.installer.exception.DbScriptException;

public interface SqlModule {

  /**
   *
   * @return the module name
   */
  String getName();

  /**
   * 
   * @throws DbScriptException
   */
  void install() throws DbScriptException;
}
