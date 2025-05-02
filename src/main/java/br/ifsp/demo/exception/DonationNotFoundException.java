package br.ifsp.demo.exception;

public class DonationNotFoundException extends RuntimeException {
    public DonationNotFoundException(String message) {
        super(message);
    }
}
