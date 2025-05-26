package br.ifsp.demo.exception;

public class ExamRequestNotAllowedException extends RuntimeException{
    public ExamRequestNotAllowedException(String message) {
        super(message);
    }
}
