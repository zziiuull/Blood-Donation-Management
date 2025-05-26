package br.ifsp.demo.presentation.config;

import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.common.ContactInfo;
import br.ifsp.demo.domain.model.common.Cpf;
import br.ifsp.demo.domain.model.donation.Appointment;
import br.ifsp.demo.domain.model.donation.AppointmentStatus;
import br.ifsp.demo.domain.model.donation.CollectionSite;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.donor.Sex;
import br.ifsp.demo.infrastructure.repository.appointment.AppointmentRepository;
import br.ifsp.demo.infrastructure.repository.collectionSite.CollectionSiteRepository;
import br.ifsp.demo.infrastructure.repository.donor.DonorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    private final DonorRepository donorRepository;
    private final AppointmentRepository appointmentRepository;
    private final CollectionSiteRepository collectionSiteRepository;

    public DataLoader(DonorRepository donorRepository, AppointmentRepository appointmentRepository, CollectionSiteRepository collectionSiteRepository) {
        this.donorRepository = donorRepository;
        this.appointmentRepository = appointmentRepository;
        this.collectionSiteRepository = collectionSiteRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        ContactInfo donorContactInfo = new ContactInfo(
                "weverton@email.com",
                "11991239867",
                "Rua da Ponte Caída, n. 101, Itaquaquecetuba/SP"
        );
        Donor savedDonor = donorRepository.save(new Donor(
                "Weverton",
                new Cpf("12345678955"),
                donorContactInfo,
                LocalDate.of(1990, 5, 20),
                85.0,
                Sex.MALE,
                BloodType.O_POS
        ));
        LOGGER.info("Donor id: {}", savedDonor.getId());

        ContactInfo siteContactInfo = new ContactInfo(
                "doesangue.sorocaba@email.com",
                "1533761530",
                "Av. Anhanguera, n. 715, Sorocaba/SP"
        );
        CollectionSite site = new CollectionSite(
                "Banco de Doação de Sorocaba",
                siteContactInfo
        );
        collectionSiteRepository.save(site);

        Appointment appointment = new Appointment(
                LocalDateTime.now().plusDays(1),
                AppointmentStatus.SCHEDULED,
                site,
                "First donation"
        );
        Appointment savedAppointment = appointmentRepository.save(appointment);
        LOGGER.info("Appointment id: {}", savedAppointment.getId());
    }
}
