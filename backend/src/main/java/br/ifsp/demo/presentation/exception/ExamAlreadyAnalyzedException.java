package br.ifsp.demo.presentation.exception;

public class ExamAlreadyAnalyzedException extends RuntimeException {
    public ExamAlreadyAnalyzedException(String message) {
        super(message);
    }
}
