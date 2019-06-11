package cz.muni.ics.kypo.commons.security.exceptions;

/**
 * @author Pavel Seda
 * @author Dominik Pilar
 */
public class SecurityException extends RuntimeException {

    public SecurityException() {
    }

    public SecurityException(String s) {
        super(s);
    }

    public SecurityException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SecurityException(Throwable throwable) {
        super(throwable);
    }

    public SecurityException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
