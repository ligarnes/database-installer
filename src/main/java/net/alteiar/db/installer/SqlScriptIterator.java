package net.alteiar.db.installer;

import java.util.Iterator;

import net.alteiar.sql.SqlScript;

class SqlScriptIterator implements Iterator<SqlScript> {

    private final Iterator<String> itt;

    private final ModuleImpl<?> module;

    public SqlScriptIterator(ModuleImpl<?> module) {

        this.module = module;
        this.itt = module.getScriptNameList().iterator();
    }

    @Override
    public boolean hasNext() {

        return itt.hasNext();
    }

    @Override
    public SqlScript next() {

        String next = itt.next();

        return module.getScript(next);
    }

}