package scraper.exception;

import scraper.ErrorType;

public class InstagramAuthException extends InstagramException {

    public InstagramAuthException() {
    }

    public InstagramAuthException(String message) {
        super(message, ErrorType.UNKNOWN_ERROR);
    }

    public InstagramAuthException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}
