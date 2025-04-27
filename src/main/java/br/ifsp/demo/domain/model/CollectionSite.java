package br.ifsp.demo.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
public class CollectionSite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private UUID id;

    @Getter
    private String name;

    @Embedded
    @Getter
    private ContactInfo contactInfo;

    protected CollectionSite() {}

    public CollectionSite(String name, ContactInfo contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
    }


}
