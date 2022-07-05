package scraper.exception;

import scraper.ErrorType;

public class InstagramNotFoundException extends InstagramException {

    public InstagramNotFoundException() {
    }

    public InstagramNotFoundException(String message) {
        super(message, ErrorType.UNKNOWN_ERROR);
    }

    public InstagramNotFoundException(String message, ErrorType errorType) {
        super(message, errorType);
    }
}
