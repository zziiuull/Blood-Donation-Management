package br.ifsp.demo.exception;

public class ExamAlreadyAnalyzedException extends RuntimeException {
    public ExamAlreadyAnalyzedException(String message) {
        super(message);
    }
}
