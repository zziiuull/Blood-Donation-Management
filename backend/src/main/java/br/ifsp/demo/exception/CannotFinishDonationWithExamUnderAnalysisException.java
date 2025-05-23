package br.ifsp.demo.exception;

public class CannotFinishDonationWithExamUnderAnalysisException extends RuntimeException {
    public CannotFinishDonationWithExamUnderAnalysisException(String message) {
        super(message);
    }
}
