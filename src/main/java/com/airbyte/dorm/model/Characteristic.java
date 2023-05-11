package com.airbyte.dorm.model;

import com.airbyte.dorm.common.TimeConverter;
import com.airbyte.dorm.model.enums.Gender;
import com.airbyte.dorm.model.enums.MaritalStatus;
import com.airbyte.dorm.model.enums.RegistrationType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Table(name = "characteristic")
@Entity(name = "characteristic")
public class Characteristic implements Serializable {
    private @Id
    @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @Column(columnDefinition = "VARCHAR(50)") String firstName;
    private @Column(columnDefinition = "VARCHAR(50)") String lastName;
    private @Column(columnDefinition = "VARCHAR(50)") String nationalCode;
    private @Column(columnDefinition = "VARCHAR(50)") String certificateNumber;
    private @Column(columnDefinition = "VARCHAR(15)") String phoneNumber;
    private @Column(columnDefinition = "VARCHAR(1000)") String address;
    private @Column(columnDefinition = "VARCHAR(20)") String telephoneNumber;
    private @Column(columnDefinition = "VARCHAR(50)") String fatherName;
    private @Column(columnDefinition = "VARCHAR(20)") String emergencyNumber;
    private @Column(columnDefinition = "VARCHAR(20)") String birthPlace;
    private @Column(columnDefinition = "TIMESTAMP") Date birthDate;
    private @Column(columnDefinition = "VARCHAR(50)") String job;
    private @Column(columnDefinition = "VARCHAR(50)") String education;
    private @Column(columnDefinition = "VARCHAR(20)") String postalCode;
    private @Column(columnDefinition = "VARCHAR(50)") String email;
    private @Column(columnDefinition = "VARCHAR(50)") String nationality;
    private @Enumerated(EnumType.STRING)
    MaritalStatus maritalStatus;
    private @Column(columnDefinition = "VARCHAR(20)") String religion;
    private @Column(columnDefinition = "VARCHAR(20)") String subReligion;
    private @Column(columnDefinition = "VARCHAR(1000)") String healthyStatus;
    private @Column(columnDefinition = "BOOLEAN DEFAULT TRUE") Boolean health;
    private @Column(columnDefinition = "VARCHAR(50)") String alias;
    private @Column(columnDefinition = "TIMESTAMP") Date reservationDate;
    private @Column(columnDefinition = "VARCHAR(50)") String PlaceOfIssue;
    private @Column(columnDefinition = "VARCHAR(50)") String university;
    private @Column(columnDefinition = "VARCHAR(50)") String studentNumber;
    private @Column(columnDefinition = "VARCHAR(50)") String major;
    private @Column(columnDefinition = "VARCHAR(100)") String spouseFullName;
    private @Column(columnDefinition = "VARCHAR(50)") String spouseFatherName;
    private @Column(columnDefinition = "VARCHAR(50)") String spouseJob;
    private @Column(columnDefinition = "VARCHAR(50)") String fatherJob;
    private @Column(columnDefinition = "VARCHAR(1000)") String parentAddress;
    private @Column(columnDefinition = "VARCHAR(50)") String homeNumber;
    private @Column(columnDefinition = "VARCHAR(50)") String motherPhoneNumber;
    private @Column(columnDefinition = "VARCHAR(50)") String fatherPhoneNumber;
    private @Column(columnDefinition = "VARCHAR(50)") String bankName;
    private @Column(columnDefinition = "VARCHAR(50)") String cardNumber;
    private @Column(columnDefinition = "VARCHAR(50)") String bankAccountNumber;
    private @Column(columnDefinition = "VARCHAR(50)") String bankAccountOwnerName;
    private @Column(columnDefinition = "VARCHAR(50)") String bankAccountShabaNumber;
    private @Column(columnDefinition = "VARCHAR(50)") Date bankAccountExpirationDate;
    private @Column(columnDefinition = "VARCHAR(50)") String cvv2;
    private @Column(columnDefinition = "VARCHAR(255)") String fullName;
    private @Column(columnDefinition = "VARCHAR(255)") String parentType;
    private @Column(columnDefinition = "VARCHAR(50)") String parentId;
    private @Column(columnDefinition = "VARCHAR(255)") String firstPersonFullName;
    private @Column(columnDefinition = "VARCHAR(255)") String firstPersonPhoneNumber;
    private @Column(columnDefinition = "VARCHAR(255)") String firstPersonFatherName;
    private @Column(columnDefinition = "VARCHAR(255)") String firstPersonRelationshipWithResident;
    private @Column(columnDefinition = "VARCHAR(255)") String secondPersonFullName;
    private @Column(columnDefinition = "VARCHAR(255)") String secondPersonPhoneNumber;
    private @Column(columnDefinition = "VARCHAR(255)") String secondPersonFatherName;
    private @Column(columnDefinition = "VARCHAR(255)") String secondPersonRelationshipWithResident;
    private @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(100)") RegistrationType registrationType;
    private @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50)") Gender gender;
    private @Embedded
    TimePeriod timePeriod = new TimePeriod();
    private @Column(columnDefinition = "TIMESTAMP") Date paymentDate;
    private @Column(columnDefinition = "VARCHAR(25)") String rentPaymentAmount;
    private @Column(columnDefinition = "VARCHAR(30)") String depositPaymentAmount;
    private @Column(columnDefinition = "VARCHAR(30)") String discountPaymentAmount;
    private @Column(columnDefinition = "VARCHAR(30)") String relationshipWithResident;

    private @Column(columnDefinition = "VARCHAR(50)") String profileId;
    private @Column(columnDefinition = "VARCHAR(50)") String personType;

    public Characteristic() {
    }

    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getSubReligion() {
        return subReligion;
    }

    public void setSubReligion(String subReligion) {
        this.subReligion = subReligion;
    }

    public String getHealthyStatus() {
        return healthyStatus;
    }

    public void setHealthyStatus(String healthyStatus) {
        this.healthyStatus = healthyStatus;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public Boolean getHealth() {
        return health;
    }

    public void setHealth(Boolean health) {
        this.health = health;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPlaceOfIssue() {
        return PlaceOfIssue;
    }

    public void setPlaceOfIssue(String placeOfIssue) {
        PlaceOfIssue = placeOfIssue;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSpouseFullName() {
        return spouseFullName;
    }

    public void setSpouseFullName(String spouseFullName) {
        this.spouseFullName = spouseFullName;
    }

    public String getSpouseFatherName() {
        return spouseFatherName;
    }

    public void setSpouseFatherName(String spouseFatherName) {
        this.spouseFatherName = spouseFatherName;
    }

    public String getSpouseJob() {
        return spouseJob;
    }

    public void setSpouseJob(String spouseJob) {
        this.spouseJob = spouseJob;
    }

    public String getFatherJob() {
        return fatherJob;
    }

    public void setFatherJob(String fatherJob) {
        this.fatherJob = fatherJob;
    }

    public String getParentAddress() {
        return parentAddress;
    }

    public void setParentAddress(String parentAddress) {
        this.parentAddress = parentAddress;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getMotherPhoneNumber() {
        return motherPhoneNumber;
    }

    public void setMotherPhoneNumber(String motherPhoneNumber) {
        this.motherPhoneNumber = motherPhoneNumber;
    }

    public String getFatherPhoneNumber() {
        return fatherPhoneNumber;
    }

    public void setFatherPhoneNumber(String fatherPhoneNumber) {

        this.fatherPhoneNumber = fatherPhoneNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankAccountOwnerName() {
        return bankAccountOwnerName;
    }

    public void setBankAccountOwnerName(String bankAccountOwnerName) {
        this.bankAccountOwnerName = bankAccountOwnerName;
    }

    public String getBankAccountShabaNumber() {
        return bankAccountShabaNumber;
    }

    public void setBankAccountShabaNumber(String bankAccountShabaNumber) {
        this.bankAccountShabaNumber = bankAccountShabaNumber;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public String getBirthDate() {
        return TimeConverter.convert(this.birthDate, TimeConverter.DEFAULT_PATTERN_FORMAT);
    }

    @JsonIgnore
    public Date birthDate() {
        return this.birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @JsonIgnore
    public Date reservationDate() {
        return this.reservationDate;
    }

    public String getReservationDate() {
        return TimeConverter.convert(this.reservationDate, TimeConverter.DEFAULT_PATTERN_FORMAT);
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    @JsonIgnore
    public Date bankAccountExpirationDate() {
        return this.bankAccountExpirationDate;
    }

    public void setBankAccountExpirationDate(Date bankAccountExpirationDate) {
        this.bankAccountExpirationDate = bankAccountExpirationDate;
    }

    public String getBankAccountExpirationDate() {
        return TimeConverter.convert(this.bankAccountExpirationDate, TimeConverter.DEFAULT_PATTERN_FORMAT);
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getFirstPersonFullName() {
        return firstPersonFullName;
    }

    public void setFirstPersonFullName(String firstPersonFullName) {
        this.firstPersonFullName = firstPersonFullName;
    }

    public String getFirstPersonPhoneNumber() {
        return firstPersonPhoneNumber;
    }

    public void setFirstPersonPhoneNumber(String firstPersonPhoneNumber) {
        this.firstPersonPhoneNumber = firstPersonPhoneNumber;
    }

    public String getFirstPersonFatherName() {
        return firstPersonFatherName;
    }

    public void setFirstPersonFatherName(String firstPersonFatherName) {
        this.firstPersonFatherName = firstPersonFatherName;
    }

    public String getFirstPersonRelationshipWithResident() {
        return firstPersonRelationshipWithResident;
    }

    public void setFirstPersonRelationshipWithResident(String firstPersonRelationshipWithResident) {
        this.firstPersonRelationshipWithResident = firstPersonRelationshipWithResident;
    }

    public String getSecondPersonFullName() {
        return secondPersonFullName;
    }

    public void setSecondPersonFullName(String secondPersonFullName) {
        this.secondPersonFullName = secondPersonFullName;
    }

    public String getSecondPersonPhoneNumber() {
        return secondPersonPhoneNumber;
    }

    public void setSecondPersonPhoneNumber(String secondPersonPhoneNumber) {
        this.secondPersonPhoneNumber = secondPersonPhoneNumber;
    }

    public String getSecondPersonFatherName() {
        return secondPersonFatherName;
    }

    public void setSecondPersonFatherName(String secondPersonFatherName) {
        this.secondPersonFatherName = secondPersonFatherName;
    }

    public String getSecondPersonRelationshipWithResident() {
        return secondPersonRelationshipWithResident;
    }

    public void setSecondPersonRelationshipWithResident(String secondPersonRelationshipWithResident) {
        this.secondPersonRelationshipWithResident = secondPersonRelationshipWithResident;
    }

    public RegistrationType getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(RegistrationType registrationType) {
        this.registrationType = registrationType;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getPaymentDate() {
        return TimeConverter.convert(this.paymentDate, TimeConverter.DEFAULT_PATTERN_FORMAT);
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    @JsonIgnore
    public Date paymentDate() {
        return this.paymentDate;
    }

    public String getRentPaymentAmount() {
        return rentPaymentAmount;
    }

    public void setRentPaymentAmount(String rentPaymentAmount) {
        this.rentPaymentAmount = rentPaymentAmount;
    }

    public String getDepositPaymentAmount() {
        return depositPaymentAmount;
    }

    public void setDepositPaymentAmount(String depositPaymentAmount) {
        this.depositPaymentAmount = depositPaymentAmount;
    }

    public String getDiscountPaymentAmount() {
        return discountPaymentAmount;
    }

    public void setDiscountPaymentAmount(String discountPaymentAmount) {
        this.discountPaymentAmount = discountPaymentAmount;
    }

    public String getRelationshipWithResident() {
        return relationshipWithResident;
    }

    public void setRelationshipWithResident(String relationshipWithResident) {
        this.relationshipWithResident = relationshipWithResident;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }
}
