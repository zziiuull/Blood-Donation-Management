package br.ifsp.demo.presentation.config;

import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.common.ContactInfo;
import br.ifsp.demo.domain.model.common.Cpf;
import br.ifsp.demo.domain.model.donation.*;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.donor.Sex;
import br.ifsp.demo.domain.model.physician.Crm;
import br.ifsp.demo.domain.model.physician.Physician;
import br.ifsp.demo.domain.model.physician.State;
import br.ifsp.demo.infrastructure.repository.appointment.AppointmentRepository;
import br.ifsp.demo.infrastructure.repository.collectionSite.CollectionSiteRepository;
import br.ifsp.demo.infrastructure.repository.donor.DonorRepository;
import br.ifsp.demo.presentation.security.user.JpaUserRepository;
import br.ifsp.demo.presentation.security.user.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DataLoader implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    private final DonorRepository donorRepository;
    private final AppointmentRepository appointmentRepository;
    private final CollectionSiteRepository collectionSiteRepository;
    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(
        DonorRepository donorRepository,
        AppointmentRepository appointmentRepository,
        CollectionSiteRepository collectionSiteRepository,
        JpaUserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.donorRepository = donorRepository;
        this.appointmentRepository = appointmentRepository;
        this.collectionSiteRepository = collectionSiteRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        CollectionSite site = new CollectionSite(
                "Banco de Doação de Sorocaba",
                new ContactInfo("doesangue.sorocaba@email.com", "1533761530", "Av. Anhanguera, n. 715, Sorocaba/SP")
        );
        CollectionSite savedCollectionSite = collectionSiteRepository.save(site);
        LOGGER.info("CollectionSite id: {}", savedCollectionSite.getId());

        Donor donor1 = new Donor(
                "Weverton",
                new Cpf("12345678955"),
                new ContactInfo("weverton@email.com", "11991239867", "Rua da Ponte Caída, n. 101, Itaquaquecetuba/SP"),
                LocalDate.of(1990, 5, 20),
                85.0,
                Sex.MALE,
                BloodType.O_POS
        );
        Donor savedDonor1 = donorRepository.save(donor1);
        LOGGER.info("Donor1 id: {}", savedDonor1.getId());

        Donor donor2 = new Donor(
                "Ana Beatriz",
                new Cpf("98765432100"),
                new ContactInfo("ana@email.com", "11998887766", "Rua das Flores, n. 25, Campinas/SP"),
                LocalDate.of(1995, 8, 10),
                60.0,
                Sex.FEMALE,
                BloodType.A_NEG
        );
        Donor savedDonor2 = donorRepository.save(donor2);
        LOGGER.info("Donor2 id: {}", savedDonor2.getId());

        Appointment appointment1 = new Appointment(
                LocalDateTime.now().plusDays(1),
                AppointmentStatus.SCHEDULED,
                site,
                "Primeira doação"
        );
        Appointment savedAppointment1 = appointmentRepository.save(appointment1);
        LOGGER.info("Appointment1 id: {}", savedAppointment1.getId());

        Appointment appointment2 = new Appointment(
                LocalDateTime.now().plusDays(2),
                AppointmentStatus.SCHEDULED,
                site,
                "Segunda doação"
        );
        Appointment savedAppointment2 = appointmentRepository.save(appointment2);
        LOGGER.info("Appointment2 id: {}", savedAppointment2.getId());

        Physician physician = new Physician(
                UUID.randomUUID(),
                "John",
                "Doe",
                "johndoe@email.com",
                passwordEncoder.encode("12345678"),
                Role.PHYSICIAN,
                new Cpf("12345678911"),
                new Crm("123", State.SP)
        );
        Physician savedPhysician = userRepository.save(physician);
        LOGGER.info("Physician id: {}", savedPhysician.getId());
    }
}
