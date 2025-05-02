package br.ifsp.demo.exception;

public class InvalidDonationAnalysisException extends RuntimeException{
    public InvalidDonationAnalysisException(String message) {
        super(message);
    }
}
