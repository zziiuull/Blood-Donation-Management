package br.ifsp.demo.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class ContactInfo {

    private String email;

    private String phone;

    private String address;

    protected ContactInfo() {}

    public ContactInfo(String email, String phone, String address) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Email must not be blank");
        if (phone == null || phone.isBlank())
            throw new IllegalArgumentException("Phone must not be blank");
        if (address == null || address.isBlank())
            throw new IllegalArgumentException("Address must not be blank");

        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}
