package cz.muni.ics.kypo.commons.persistence.exceptions;

/**
 * @author Pavel Seda
 */
public class PersistenceCommonsException extends RuntimeException {

    public PersistenceCommonsException() {
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

    public PersistenceCommonsException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
