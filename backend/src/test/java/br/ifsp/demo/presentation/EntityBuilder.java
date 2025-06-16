package br.ifsp.demo.presentation;

import br.ifsp.demo.domain.model.common.BloodType;
import br.ifsp.demo.domain.model.common.ContactInfo;
import br.ifsp.demo.domain.model.common.Cpf;
import br.ifsp.demo.domain.model.donation.*;
import br.ifsp.demo.domain.model.donor.Donor;
import br.ifsp.demo.domain.model.donor.Sex;
import br.ifsp.demo.domain.model.physician.Crm;
import br.ifsp.demo.domain.model.physician.Physician;
import br.ifsp.demo.domain.model.physician.State;
import br.ifsp.demo.presentation.security.auth.RegisterPhysicianRequest;
import br.ifsp.demo.presentation.security.user.Role;
import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class EntityBuilder {
    private static final Faker faker = Faker.instance();

    public static Physician createRandomPhysician(String password){
        String cpf = faker.number().digits(11);
        String crmValue = faker.number().digits(7);
        State state = State.values()[faker.random().nextInt(State.values().length)];

        return new Physician (
                UUID.randomUUID(),
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                password,
                Role.PHYSICIAN,
                new Cpf(cpf),
                new Crm(crmValue, state)
        );
    }

    public static RegisterPhysicianRequest createRandomRegisterPhysicianRequest(String password) {
        String cpf = faker.number().digits(11);
        String crmValue = faker.number().digits(7);
        State state = State.values()[faker.random().nextInt(State.values().length)];

        return new RegisterPhysicianRequest(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                password,
                cpf,
                faker.phoneNumber().cellPhone(),
                faker.address().fullAddress(),
                crmValue,
                state.name()
        );
    }

    public static Donor createRandomDonor() {
        String name = faker.name().fullName();
        Cpf cpf = new Cpf(faker.number().digits(11));
        ContactInfo contactInfo = new ContactInfo(
                faker.internet().emailAddress(),
                faker.phoneNumber().cellPhone(),
                faker.address().fullAddress()
        );

        // Age between 20 and 60 years
        LocalDate birthDate = LocalDate.now().minusYears(ThreadLocalRandom.current().nextInt(20, 60));

        double weight = ThreadLocalRandom.current().nextDouble(50.0, 100.0);
        Sex sex = Sex.values()[faker.random().nextInt(Sex.values().length)];
        BloodType bloodType = BloodType.values()[faker.random().nextInt(BloodType.values().length)];

        return new Donor(name, cpf, contactInfo, birthDate, weight, sex, bloodType);
    }

    public static Donation createRandomDonation(Donor donor, Appointment appointment) {
        return new Donation(donor, appointment, DonationStatus.UNDER_ANALYSIS);
    }

    public static ContactInfo createRandomCollectionInfo() {
        return new ContactInfo(
                faker.internet().emailAddress(),
                faker.phoneNumber().cellPhone(),
                faker.address().fullAddress()
        );
    }

    public static CollectionSite createRandomCollectionSite() {

        return new CollectionSite(
                faker.company().name(),
                createRandomCollectionInfo()
        );
    }

    public static Appointment createRandomAppointment(CollectionSite site) {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(ThreadLocalRandom.current().nextInt(1, 30));

        return new Appointment(
                futureDate,
                AppointmentStatus.SCHEDULED,
                site,
                faker.lorem().sentence()
        );
    }
}
