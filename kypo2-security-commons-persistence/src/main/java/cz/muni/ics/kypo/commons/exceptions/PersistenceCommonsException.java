package cz.muni.ics.kypo.commons.exceptions;

public class PersistenceCommonsException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public PersistenceCommonsException() {
        super();
    }

    public PersistenceCommonsException(String s) {
        super(s);
    }

    public PersistenceCommonsException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PersistenceCommonsException(Throwable throwable) {
        super(throwable);
    }
}
