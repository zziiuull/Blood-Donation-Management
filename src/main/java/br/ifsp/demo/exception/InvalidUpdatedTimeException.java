package br.ifsp.demo.exception;

public class InvalidUpdatedTimeException extends RuntimeException {
    public InvalidUpdatedTimeException(String message) {
        super(message);
    }
}
