package com.postoffice.parser.exception;

public class LineParserException extends RuntimeException {
    public LineParserException(String message) {
        super(message);
    }

    public LineParserException(String message, Exception ex) {
        super(message, ex);
    }
}
