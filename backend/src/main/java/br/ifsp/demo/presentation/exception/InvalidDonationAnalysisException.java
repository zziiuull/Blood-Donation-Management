package br.ifsp.demo.presentation.exception;

public class InvalidDonationAnalysisException extends RuntimeException{
    public InvalidDonationAnalysisException(String message) {
        super(message);
    }
}
