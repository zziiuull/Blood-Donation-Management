package br.ifsp.demo.presentation.exception;

public class CannotFinishDonationWithExamUnderAnalysisException extends RuntimeException {
    public CannotFinishDonationWithExamUnderAnalysisException(String message) {
        super(message);
    }
}
