package com.airbyte.dorm.characteristic;

import com.airbyte.dorm.common.TimeConverter;
import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.CharacteristicDTO;
import com.airbyte.dorm.model.Characteristic;
import com.airbyte.dorm.model.enums.Gender;
import com.airbyte.dorm.model.enums.MaritalStatus;
import com.airbyte.dorm.model.enums.RegistrationType;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class CharacteristicService extends ParentService<Characteristic, CharacteristicRepository, CharacteristicDTO> {

    public CharacteristicService(CharacteristicRepository repository) {
        super(repository);
    }

    @Override
    public Characteristic updateModelFromDto(Characteristic model, CharacteristicDTO dto) {
        model.setAddress(dto.getAddress() != null ? dto.getAddress() : model.getAddress());
        model.setBirthDate(dto.getBirthDate() != null ? TimeConverter.convertStringToInstant(dto.getBirthDate(), TimeConverter.DEFAULT_PATTERN_FORMAT) : model.birthDate());
        model.setBirthPlace(dto.getBirthPlace() != null ? dto.getBirthPlace() : model.getBirthPlace());
        model.setEducation(dto.getEducation() != null ? dto.getEducation() : model.getEducation());
        model.setEmail(dto.getEmail() != null ? dto.getEmail() : model.getEmail());
        model.setCertificateNumber(dto.getCertificateNumber() != null ? dto.getCertificateNumber() : model.getCertificateNumber());
        model.setFirstName(dto.getFirstName() != null ? dto.getFirstName() : model.getFirstName());
        model.setLastName(dto.getLastName() != null ? dto.getLastName() : model.getLastName());
        model.setNationalCode(dto.getNationalCode() != null ? dto.getNationalCode() : model.getNationalCode());
        model.setCertificateNumber(dto.getCertificateNumber() != null ? dto.getCertificateNumber() : model.getCertificateNumber());
        model.setPhoneNumber(dto.getPhoneNumber() != null ? dto.getPhoneNumber() : model.getPhoneNumber());
        model.setAddress(dto.getAddress() != null ? dto.getAddress() : model.getAddress());
        model.setTelephoneNumber(dto.getTelephoneNumber() != null ? dto.getTelephoneNumber() : model.getTelephoneNumber());
        model.setFatherName(dto.getFatherName() != null ? dto.getSpouseFatherName() : model.getFatherName());
        model.setEmergencyNumber(dto.getEmergencyNumber() != null ? dto.getEmergencyNumber() : model.getEmergencyNumber());
        model.setJob(dto.getJob() != null ? dto.getJob() : model.getJob());
        model.setPostalCode(dto.getPostalCode() != null ? dto.getPostalCode() : model.getPostalCode());
        model.setNationality(dto.getNationality() != null ? dto.getNationality() : model.getNationality());
        model.setMaritalStatus(dto.getMaritalStatus() != null ? MaritalStatus.valueOf(dto.getMaritalStatus()) : model.getMaritalStatus());
        model.setReligion(dto.getReligion() != null ? dto.getReligion() : model.getReligion());
        model.setSubReligion(dto.getSubReligion() != null ? dto.getSubReligion() : model.getSubReligion());
        model.setHealthyStatus(dto.getHealthyStatus() != null ? dto.getHealthyStatus() : model.getHealthyStatus());
        model.setHealth(dto.getHealth() != null ? dto.getHealth() : model.getHealth());
        model.setAlias(dto.getAlias() != null ? dto.getAlias() : model.getAlias());
        model.setReservationDate(dto.getReservationDate() != null ? TimeConverter.convertStringToInstant(dto.getReservationDate(), TimeConverter.DEFAULT_PATTERN_FORMAT) : model.reservationDate());
        model.setPlaceOfIssue(dto.getPlaceOfIssue() != null ? dto.getPlaceOfIssue() : model.getPlaceOfIssue());
        model.setUniversity(dto.getUniversity() != null ? dto.getUniversity() : model.getUniversity());
        model.setStudentNumber(dto.getStudentNumber() != null ? dto.getStudentNumber() : model.getStudentNumber());
        model.setMajor(dto.getMajor() != null ? dto.getMajor() : model.getMajor());
        model.setSpouseFullName(dto.getSpouseFullName() != null ? dto.getSpouseFullName() : model.getSpouseFullName());
        model.setSpouseFatherName(dto.getSpouseFatherName() != null ? dto.getSpouseFatherName() : model.getSpouseFatherName());
        model.setSpouseJob(dto.getSpouseJob() != null ? dto.getSpouseJob() : model.getSpouseJob());
        model.setParentAddress(dto.getParentAddress() != null ? dto.getParentAddress() : model.getParentAddress());
        model.setHomeNumber(dto.getHomeNumber() != null ? dto.getHomeNumber() : model.getHomeNumber());
        model.setMotherPhoneNumber(dto.getMotherPhoneNumber() != null ? dto.getMotherPhoneNumber() : model.getMotherPhoneNumber());
        model.setFatherPhoneNumber(dto.getFatherPhoneNumber() != null ? dto.getPhoneNumber() : model.getPhoneNumber());
        model.setBankName(dto.getBankName() != null ? dto.getBankName() : model.getBankName());
        model.setCardNumber(dto.getCardNumber() != null ? dto.getCardNumber() : model.getCardNumber());
        model.setBankAccountNumber(dto.getBankAccountNumber() != null ? dto.getBankAccountNumber() : model.getBankAccountNumber());
        model.setBankAccountOwnerName(dto.getBankAccountOwnerName() != null ? dto.getBankAccountOwnerName() : model.getBankAccountOwnerName());
        model.setBankAccountShabaNumber(dto.getBankAccountShabaNumber() != null ? dto.getBankAccountShabaNumber() : model.getBankAccountShabaNumber());
        model.setBankAccountExpirationDate(dto.getBankAccountExpirationDate() != null ? TimeConverter.convertStringToInstant(dto.getBankAccountExpirationDate(), TimeConverter.DEFAULT_PATTERN_FORMAT) : model.bankAccountExpirationDate());
        model.setCvv2(dto.getCvv2() != null ? dto.getCvv2() : model.getCvv2());
        model.setFatherJob(dto.getFatherJob() != null ? dto.getFatherJob() : model.getFatherName());
        model.setFirstPersonFullName(dto.getFirstPersonFullName() != null ? dto.getFirstPersonFullName() : model.getFirstPersonFullName());
        model.setFirstPersonFatherName(dto.getFirstPersonFatherName() != null ? dto.getFirstPersonFatherName() : model.getFirstPersonFatherName());
        model.setFirstPersonPhoneNumber(dto.getFirstPersonPhoneNumber() != null ? dto.getFirstPersonPhoneNumber() : model.getFirstPersonPhoneNumber());
        model.setFirstPersonRelationshipWithResident(dto.getFirstPersonRelationshipWithResident() != null ? dto.getFirstPersonRelationshipWithResident() : model.getFirstPersonRelationshipWithResident());
        model.setSecondPersonFullName(dto.getSecondPersonFullName() != null ? dto.getSecondPersonFullName() : model.getSecondPersonFullName());
        model.setSecondPersonFatherName(dto.getSecondPersonFatherName() != null ? dto.getSecondPersonFatherName() : model.getSecondPersonFatherName());
        model.setSecondPersonPhoneNumber(dto.getSecondPersonPhoneNumber() != null ? dto.getSecondPersonPhoneNumber() : model.getSecondPersonPhoneNumber());
        model.setSecondPersonRelationshipWithResident(dto.getSecondPersonRelationshipWithResident() != null ? dto.getSecondPersonRelationshipWithResident() : model.getSecondPersonRelationshipWithResident());
        model.setGender(dto.getGender() != null ? Gender.valueOf(dto.getGender()) : model.getGender());
        model.setRegistrationType(dto.getRegistrationType() != null ? RegistrationType.valueOf(dto.getRegistrationType()) : model.getRegistrationType());
        if (dto.getStartDate() != null) {
            model.getTimePeriod().setStartDate(TimeConverter.convertStringToInstant(dto.getStartDate(), TimeConverter.DEFAULT_PATTERN_FORMAT));
        }
        if (dto.getEndDate() != null) {
            model.getTimePeriod().setEndDate(TimeConverter.convertStringToInstant(dto.getEndDate(), TimeConverter.DEFAULT_PATTERN_FORMAT));
        }
        model.setPaymentDate(dto.getPaymentDate() != null ? TimeConverter.convertStringToInstant(dto.getPaymentDate(), TimeConverter.DEFAULT_PATTERN_FORMAT) : model.paymentDate());
        model.setRentPaymentAmount(dto.getRentPaymentAmount() != null ? dto.getRentPaymentAmount() : model.getRentPaymentAmount());
        model.setDepositPaymentAmount(dto.getDepositPaymentAmount() != null ? dto.getDepositPaymentAmount() : model.getDepositPaymentAmount());
        model.setDiscountPaymentAmount(dto.getDiscountPaymentAmount() != null ? dto.getDiscountPaymentAmount() : model.getDiscountPaymentAmount());
        model.setRelationshipWithResident(dto.getRelationshipWithResident() != null ? dto.getRelationshipWithResident() : model.getRelationshipWithResident());
        model.setProfileId(dto.getProfileId() != null ? dto.getProfileId() : model.getProfileId());
        model.setPersonType(dto.getPersonType() != null ? dto.getPersonType() : model.getPersonType());
        return model;
    }

    @Override
    public Characteristic convertDTO(CharacteristicDTO dto) {
        Characteristic characteristic = new Characteristic();
        characteristic.setAddress(dto.getAddress());
        characteristic.setBirthDate(dto.getBirthDate() != null ? TimeConverter.convertStringToInstant(dto.getBirthDate(), TimeConverter.DEFAULT_PATTERN_FORMAT) : null);
        characteristic.setBirthPlace(dto.getBirthPlace());
        characteristic.setEducation(dto.getEducation());
        characteristic.setEmail(dto.getEmail());
        characteristic.setCertificateNumber(dto.getCertificateNumber());
        characteristic.setFirstName(dto.getFirstName());
        characteristic.setLastName(dto.getLastName());
        characteristic.setNationalCode(dto.getNationalCode());
        characteristic.setCertificateNumber(dto.getCertificateNumber());
        characteristic.setPhoneNumber(dto.getPhoneNumber());
        characteristic.setAddress(dto.getAddress());
        characteristic.setTelephoneNumber(dto.getTelephoneNumber());
        characteristic.setFatherName(dto.getFatherName());
        characteristic.setEmergencyNumber(dto.getEmergencyNumber());
        characteristic.setJob(dto.getJob());
        characteristic.setPostalCode(dto.getPostalCode());
        characteristic.setNationality(dto.getNationality());
        characteristic.setMaritalStatus(dto.getMaritalStatus() != null ? MaritalStatus.valueOf(dto.getMaritalStatus()) : null);
        characteristic.setReligion(dto.getReligion());
        characteristic.setSubReligion(dto.getSubReligion());
        characteristic.setHealthyStatus(dto.getHealthyStatus());
        characteristic.setHealth(dto.getHealth());
        characteristic.setAlias(dto.getAlias());
        characteristic.setReservationDate(dto.getReservationDate() != null ? TimeConverter.convertStringToInstant(dto.getReservationDate(), TimeConverter.DEFAULT_PATTERN_FORMAT) : null);
        characteristic.setPlaceOfIssue(dto.getPlaceOfIssue());
        characteristic.setUniversity(dto.getUniversity());
        characteristic.setStudentNumber(dto.getStudentNumber());
        characteristic.setMajor(dto.getMajor());
        characteristic.setSpouseFullName(dto.getSpouseFullName());
        characteristic.setSpouseFatherName(dto.getSpouseFatherName());
        characteristic.setSpouseJob(dto.getSpouseJob());
        characteristic.setParentAddress(dto.getParentAddress());
        characteristic.setHomeNumber(dto.getHomeNumber());
        characteristic.setMotherPhoneNumber(dto.getMotherPhoneNumber());
        characteristic.setFatherPhoneNumber(dto.getFatherPhoneNumber());
        characteristic.setBankName(dto.getBankName());
        characteristic.setCardNumber(dto.getCardNumber());
        characteristic.setBankAccountNumber(dto.getBankAccountNumber());
        characteristic.setBankAccountOwnerName(dto.getBankAccountOwnerName());
        characteristic.setBankAccountShabaNumber(dto.getBankAccountShabaNumber());
        characteristic.setBankAccountExpirationDate(dto.getBankAccountExpirationDate() != null ? TimeConverter.convertStringToInstant(dto.getBankAccountExpirationDate(), TimeConverter.DEFAULT_PATTERN_FORMAT) : null);
        characteristic.setCvv2(dto.getCvv2());
        characteristic.setFatherJob(dto.getFatherJob());
        if (dto.getFullName() == null || dto.getFullName().isEmpty() || dto.getFullName().contains("null")) {
            characteristic.setFullName(dto.getFirstName() + " " + dto.getLastName());
        } else {
            characteristic.setFullName(dto.getFullName());
        }
        characteristic.setParentType(dto.getParentType());
        characteristic.setParentId(dto.getParentId());
        characteristic.setFirstPersonFullName(dto.getFirstPersonFullName());
        characteristic.setFirstPersonFatherName(dto.getFirstPersonFatherName());
        characteristic.setFirstPersonPhoneNumber(dto.getFirstPersonPhoneNumber());
        characteristic.setFirstPersonRelationshipWithResident(dto.getFirstPersonRelationshipWithResident());
        characteristic.setSecondPersonFullName(dto.getSecondPersonFullName());
        characteristic.setSecondPersonFatherName(dto.getSecondPersonFatherName());
        characteristic.setSecondPersonPhoneNumber(dto.getSecondPersonPhoneNumber());
        characteristic.setSecondPersonRelationshipWithResident(dto.getSecondPersonRelationshipWithResident());
        characteristic.setGender(dto.getGender() != null ? Gender.valueOf(dto.getGender()) : null);
        characteristic.setRegistrationType(dto.getRegistrationType() != null ? RegistrationType.valueOf(dto.getRegistrationType()) : null);
        if (dto.getStartDate() != null) {
            characteristic.getTimePeriod().setStartDate(TimeConverter.convertStringToInstant(dto.getStartDate(), TimeConverter.DEFAULT_PATTERN_FORMAT));
        }
        if (dto.getEndDate() != null) {
            characteristic.getTimePeriod().setEndDate(TimeConverter.convertStringToInstant(dto.getEndDate(), TimeConverter.DEFAULT_PATTERN_FORMAT));
        }
        characteristic.setPaymentDate(TimeConverter.convertStringToInstant(dto.getPaymentDate(), TimeConverter.DEFAULT_PATTERN_FORMAT));
        characteristic.setRentPaymentAmount(dto.getRentPaymentAmount());
        characteristic.setDepositPaymentAmount(dto.getDepositPaymentAmount());
        characteristic.setDiscountPaymentAmount(dto.getDiscountPaymentAmount());
        characteristic.setRelationshipWithResident(dto.getRelationshipWithResident());
        characteristic.setPersonType(dto.getPersonType());
        characteristic.setProfileId(dto.getProfileId());
        return characteristic;
    }

    @Override
    public List<Characteristic> getWithSearch(CharacteristicDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Characteristic> criteriaBuilderQuery = criteriaBuilder.createQuery(Characteristic.class);

        Root<Characteristic> characteristic = criteriaBuilderQuery.from(Characteristic.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getFirstName() != null) {
            predicates.add(criteriaBuilder.like(characteristic.get("firstName"), "%" + search.getFirstName() + "%"));
        }
        if (search.getLastName() != null) {
            predicates.add(criteriaBuilder.equal(characteristic.get("lastName"), search.getLastName()));
        }
        if (search.getFatherName() != null) {
            predicates.add(criteriaBuilder.like(characteristic.get("fatherName"), search.getFatherName()));
        }
        if (search.getBankName() != null) {
            predicates.add(criteriaBuilder.equal(characteristic.get("bankName"), search.getBankName()));
        }
        if (search.getBankAccountOwnerName() != null) {
            predicates.add(criteriaBuilder.equal(characteristic.get("bankAccountOwnerName"), search.getBankAccountOwnerName()));
        }
        if (search.getNationalCode() != null) {
            predicates.add(criteriaBuilder.like(characteristic.get("nationalCode"), "%" + search.getNationalCode() + "%"));
        }
        if (search.getPhoneNumber() != null && !search.getPhoneNumber().isEmpty()) {
            predicates.add(criteriaBuilder.like(characteristic.get("phoneNumber"), "%" + search.getPhoneNumber() + "%"));
        }
        if (search.getFullName() != null && !search.getFullName().isEmpty()) {
            predicates.add(criteriaBuilder.like(characteristic.get("fullName"), "%" + search.getFullName() + "%"));
        }
        if (search.getParentType() != null && !search.getParentType().isEmpty()) {
            predicates.add(criteriaBuilder.equal(characteristic.get("parentType"), search.getParentType()));
        }
        if (search.getPersonType() != null && !search.getPersonType().isEmpty()) {
            predicates.add(criteriaBuilder.equal(characteristic.get("personType"), search.getPersonType()));
        }
        if (search.getParentId() != null && !search.getParentId().isEmpty()) {
            predicates.add(criteriaBuilder.equal(characteristic.get("parentId"), search.getParentId()));
        }

        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(criteriaBuilderQuery).getResultList();
    }

}