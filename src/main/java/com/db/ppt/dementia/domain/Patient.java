package com.db.ppt.dementia.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Patient.
 */
@Entity
@Table(name = "patient")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Max(value = 120)
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "gender")
    private String gender;

    @NotNull
    @Column(name = "relationship", nullable = false)
    private String relationship;

    @NotNull
    @Column(name = "stage", nullable = false)
    private String stage;

    @Column(name = "caregiver_notes")
    private String caregiverNotes;

    @Column(name = "primary_concerns")
    private String primaryConcerns;

    @Column(name = "medical_history")
    private String medicalHistory;

    @Column(name = "medication_details")
    private String medicationDetails;

    @NotNull
    @Column(name = "emergency_contact", nullable = false)
    private String emergencyContact;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user_petient;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Patient id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Patient firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Patient lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return this.age;
    }

    public Patient age(Integer age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return this.email;
    }

    public Patient email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return this.gender;
    }

    public Patient gender(String gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelationship() {
        return this.relationship;
    }

    public Patient relationship(String relationship) {
        this.setRelationship(relationship);
        return this;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getStage() {
        return this.stage;
    }

    public Patient stage(String stage) {
        this.setStage(stage);
        return this;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getCaregiverNotes() {
        return this.caregiverNotes;
    }

    public Patient caregiverNotes(String caregiverNotes) {
        this.setCaregiverNotes(caregiverNotes);
        return this;
    }

    public void setCaregiverNotes(String caregiverNotes) {
        this.caregiverNotes = caregiverNotes;
    }

    public String getPrimaryConcerns() {
        return this.primaryConcerns;
    }

    public Patient primaryConcerns(String primaryConcerns) {
        this.setPrimaryConcerns(primaryConcerns);
        return this;
    }

    public void setPrimaryConcerns(String primaryConcerns) {
        this.primaryConcerns = primaryConcerns;
    }

    public String getMedicalHistory() {
        return this.medicalHistory;
    }

    public Patient medicalHistory(String medicalHistory) {
        this.setMedicalHistory(medicalHistory);
        return this;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getMedicationDetails() {
        return this.medicationDetails;
    }

    public Patient medicationDetails(String medicationDetails) {
        this.setMedicationDetails(medicationDetails);
        return this;
    }

    public void setMedicationDetails(String medicationDetails) {
        this.medicationDetails = medicationDetails;
    }

    public String getEmergencyContact() {
        return this.emergencyContact;
    }

    public Patient emergencyContact(String emergencyContact) {
        this.setEmergencyContact(emergencyContact);
        return this;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public User getUser_petient() {
        return this.user_petient;
    }

    public void setUser_petient(User user) {
        this.user_petient = user;
    }

    public Patient user_petient(User user) {
        this.setUser_petient(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return getId() != null && getId().equals(((Patient) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", age=" + getAge() +
            ", email='" + getEmail() + "'" +
            ", gender='" + getGender() + "'" +
            ", relationship='" + getRelationship() + "'" +
            ", stage='" + getStage() + "'" +
            ", caregiverNotes='" + getCaregiverNotes() + "'" +
            ", primaryConcerns='" + getPrimaryConcerns() + "'" +
            ", medicalHistory='" + getMedicalHistory() + "'" +
            ", medicationDetails='" + getMedicationDetails() + "'" +
            ", emergencyContact='" + getEmergencyContact() + "'" +
            "}";
    }
}
