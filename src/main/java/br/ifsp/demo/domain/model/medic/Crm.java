package br.ifsp.demo.domain.model.medic;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import java.io.Serializable;

@Getter
@Embeddable
public class Crm implements Serializable {
    String value;
    State state;

    public Crm() {
    }

    public Crm(String value, State state) {
        this.value = value;
        this.state = state;
    }
}
