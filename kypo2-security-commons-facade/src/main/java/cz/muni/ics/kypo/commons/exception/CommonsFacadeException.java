package cz.muni.ics.kypo.commons.exception;

public class CommonsFacadeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CommonsFacadeException() {
        super();
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
}
