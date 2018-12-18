package cz.muni.ics.kypo.commons.facade.exception;

/**
 * @author Pavel Seda
 */
public class CommonsFacadeException extends RuntimeException {

    public CommonsFacadeException() {
    }

    public CommonsFacadeException(String s) {
        super(s);
    }

    public CommonsFacadeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CommonsFacadeException(Throwable throwable) {
        super(throwable);
    }

    public CommonsFacadeException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
