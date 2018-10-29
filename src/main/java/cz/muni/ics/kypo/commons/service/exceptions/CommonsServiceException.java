package cz.muni.ics.kypo.commons.service.exceptions;

public class CommonsServiceException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public CommonsServiceException() {
        super();
    }

    public CommonsServiceException(String s) {
        super(s);
    }

    public CommonsServiceException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public CommonsServiceException(Throwable throwable) {
        super(throwable);
    }
}
