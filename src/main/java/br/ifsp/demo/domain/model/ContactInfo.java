package br.ifsp.demo.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
public class ContactInfo {

    @Getter
    private String email;

    @Getter
    private String phone;

    @Getter
    private String address;

    protected ContactInfo() {}

    public ContactInfo(String email, String phone, String address) {
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}
