package br.ifsp.demo.domain.model.donation;

import br.ifsp.demo.domain.model.common.ContactInfo;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
public class CollectionSite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Embedded
    private ContactInfo contactInfo;

    protected CollectionSite() {}

    public CollectionSite(String name, ContactInfo contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
    }


}
