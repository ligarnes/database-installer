package net.alteiar.db.installer.exception;

public class DbScriptException extends Exception {

    private static final long serialVersionUID = 1L;

    public DbScriptException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbScriptException(String message) {
        super(message);
    }

    public DbScriptException(Throwable cause) {
        super(cause);
    }

}
