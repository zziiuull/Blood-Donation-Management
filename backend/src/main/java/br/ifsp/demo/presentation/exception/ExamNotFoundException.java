package br.ifsp.demo.presentation.exception;

public class ExamNotFoundException extends RuntimeException{
    public ExamNotFoundException(String message) {
        super(message);
    }
}
