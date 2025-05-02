package br.ifsp.demo.application.service.notifier;

import br.ifsp.demo.domain.model.donor.Donor;
import org.springframework.stereotype.Service;

@Service
public class NotifierServiceFake implements NotifierService {
    @Override
    public void notify(Donor donor, String msg) {
        System.out.println(donor.getName() + " " + msg);
    }
}
