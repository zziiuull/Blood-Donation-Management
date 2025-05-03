package br.ifsp.demo.application.service.notifier;

import br.ifsp.demo.domain.model.donor.Donor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class NotifierServiceFake implements NotifierService {
    private static final Logger LOGGER =  LoggerFactory.getLogger(NotifierServiceFake.class);

    @Override
    public void notify(Donor donor, String msg) {
        LOGGER.info("{} {}", donor.getName(), msg);
    }
}
