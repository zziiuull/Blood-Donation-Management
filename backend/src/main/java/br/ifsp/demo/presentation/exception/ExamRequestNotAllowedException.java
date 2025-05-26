package br.ifsp.demo.presentation.exception;

public class ExamRequestNotAllowedException extends RuntimeException{
    public ExamRequestNotAllowedException(String message) {
        super(message);
    }
}
