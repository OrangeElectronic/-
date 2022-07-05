package scraper.exception;

import scraper.ErrorType;

import java.io.IOException;

public class InstagramException extends IOException {
	
    public ErrorType errorType;

    public InstagramException() {
    }

    public InstagramException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " : " + getErrorType();
    }
}
