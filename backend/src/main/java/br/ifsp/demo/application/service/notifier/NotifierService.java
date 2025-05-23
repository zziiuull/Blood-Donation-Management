package br.ifsp.demo.application.service.notifier;

import br.ifsp.demo.domain.model.donor.Donor;

public interface NotifierService {
    void notify(Donor donor, String msg);
}
