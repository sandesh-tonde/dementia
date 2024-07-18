package com.db.ppt.dementia.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PatientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Patient getPatientSample1() {
        return new Patient()
            .id(1L)
            .firstName("firstName1")
            .lastName("lastName1")
            .age(1)
            .email("email1")
            .gender("gender1")
            .relationship("relationship1")
            .stage("stage1")
            .caregiverNotes("caregiverNotes1")
            .primaryConcerns("primaryConcerns1")
            .medicalHistory("medicalHistory1")
            .medicationDetails("medicationDetails1")
            .emergencyContact("emergencyContact1");
    }

    public static Patient getPatientSample2() {
        return new Patient()
            .id(2L)
            .firstName("firstName2")
            .lastName("lastName2")
            .age(2)
            .email("email2")
            .gender("gender2")
            .relationship("relationship2")
            .stage("stage2")
            .caregiverNotes("caregiverNotes2")
            .primaryConcerns("primaryConcerns2")
            .medicalHistory("medicalHistory2")
            .medicationDetails("medicationDetails2")
            .emergencyContact("emergencyContact2");
    }

    public static Patient getPatientRandomSampleGenerator() {
        return new Patient()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .age(intCount.incrementAndGet())
            .email(UUID.randomUUID().toString())
            .gender(UUID.randomUUID().toString())
            .relationship(UUID.randomUUID().toString())
            .stage(UUID.randomUUID().toString())
            .caregiverNotes(UUID.randomUUID().toString())
            .primaryConcerns(UUID.randomUUID().toString())
            .medicalHistory(UUID.randomUUID().toString())
            .medicationDetails(UUID.randomUUID().toString())
            .emergencyContact(UUID.randomUUID().toString());
    }
}
