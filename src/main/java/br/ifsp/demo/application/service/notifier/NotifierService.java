package br.ifsp.demo.application.service.notifier;

import br.ifsp.demo.domain.model.Donor;

public interface NotifierService {
    void notify(Donor donor, String msg);
}
