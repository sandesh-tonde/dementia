package com.db.ppt.dementia.web.rest;

import static com.db.ppt.dementia.domain.PatientAsserts.*;
import static com.db.ppt.dementia.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.db.ppt.dementia.IntegrationTest;
import com.db.ppt.dementia.domain.Patient;
import com.db.ppt.dementia.domain.User;
import com.db.ppt.dementia.repository.PatientRepository;
import com.db.ppt.dementia.repository.UserRepository;
import com.db.ppt.dementia.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PatientResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PatientResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 120;
    private static final Integer UPDATED_AGE = 119;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_RELATIONSHIP = "AAAAAAAAAA";
    private static final String UPDATED_RELATIONSHIP = "BBBBBBBBBB";

    private static final String DEFAULT_STAGE = "AAAAAAAAAA";
    private static final String UPDATED_STAGE = "BBBBBBBBBB";

    private static final String DEFAULT_CAREGIVER_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_CAREGIVER_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMARY_CONCERNS = "AAAAAAAAAA";
    private static final String UPDATED_PRIMARY_CONCERNS = "BBBBBBBBBB";

    private static final String DEFAULT_MEDICAL_HISTORY = "AAAAAAAAAA";
    private static final String UPDATED_MEDICAL_HISTORY = "BBBBBBBBBB";

    private static final String DEFAULT_MEDICATION_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_MEDICATION_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_EMERGENCY_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_EMERGENCY_CONTACT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/patients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private PatientRepository patientRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientMockMvc;

    private Patient patient;

    private Patient insertedPatient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createEntity(EntityManager em) {
        Patient patient = new Patient()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .age(DEFAULT_AGE)
            .email(DEFAULT_EMAIL)
            .gender(DEFAULT_GENDER)
            .relationship(DEFAULT_RELATIONSHIP)
            .stage(DEFAULT_STAGE)
            .caregiverNotes(DEFAULT_CAREGIVER_NOTES)
            .primaryConcerns(DEFAULT_PRIMARY_CONCERNS)
            .medicalHistory(DEFAULT_MEDICAL_HISTORY)
            .medicationDetails(DEFAULT_MEDICATION_DETAILS)
            .emergencyContact(DEFAULT_EMERGENCY_CONTACT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        patient.setUser_petient(user);
        return patient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createUpdatedEntity(EntityManager em) {
        Patient patient = new Patient()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .age(UPDATED_AGE)
            .email(UPDATED_EMAIL)
            .gender(UPDATED_GENDER)
            .relationship(UPDATED_RELATIONSHIP)
            .stage(UPDATED_STAGE)
            .caregiverNotes(UPDATED_CAREGIVER_NOTES)
            .primaryConcerns(UPDATED_PRIMARY_CONCERNS)
            .medicalHistory(UPDATED_MEDICAL_HISTORY)
            .medicationDetails(UPDATED_MEDICATION_DETAILS)
            .emergencyContact(UPDATED_EMERGENCY_CONTACT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        patient.setUser_petient(user);
        return patient;
    }

    @BeforeEach
    public void initTest() {
        patient = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPatient != null) {
            patientRepository.delete(insertedPatient);
            insertedPatient = null;
        }
    }

    @Test
    @Transactional
    void createPatient() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Patient
        var returnedPatient = om.readValue(
            restPatientMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patient)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Patient.class
        );

        // Validate the Patient in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPatientUpdatableFieldsEquals(returnedPatient, getPersistedPatient(returnedPatient));

        assertPatientMapsIdRelationshipPersistedValue(patient, returnedPatient);

        insertedPatient = returnedPatient;
    }

    @Test
    @Transactional
    void createPatientWithExistingId() throws Exception {
        // Create the Patient with an existing ID
        patient.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void updatePatientMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Add a new parent entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();

        // Load the patient
        Patient updatedPatient = patientRepository.findById(patient.getId()).orElseThrow();
        assertThat(updatedPatient).isNotNull();
        // Disconnect from session so that the updates on updatedPatient are not directly saved in db
        em.detach(updatedPatient);

        // Update the User with new association value
        updatedPatient.setUser_petient(user);

        // Update the entity
        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPatient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        /**
         * Validate the id for MapsId, the ids must be same
         * Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
         * Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
         * assertThat(testPatient.getId()).isEqualTo(testPatient.getUser().getId());
         */
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        patient.setFirstName(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patient)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        patient.setLastName(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patient)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAgeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        patient.setAge(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patient)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        patient.setEmail(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patient)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelationshipIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        patient.setRelationship(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patient)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        patient.setStage(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patient)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmergencyContactIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        patient.setEmergencyContact(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patient)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPatients() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);

        // Get all the patientList
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].relationship").value(hasItem(DEFAULT_RELATIONSHIP)))
            .andExpect(jsonPath("$.[*].stage").value(hasItem(DEFAULT_STAGE)))
            .andExpect(jsonPath("$.[*].caregiverNotes").value(hasItem(DEFAULT_CAREGIVER_NOTES)))
            .andExpect(jsonPath("$.[*].primaryConcerns").value(hasItem(DEFAULT_PRIMARY_CONCERNS)))
            .andExpect(jsonPath("$.[*].medicalHistory").value(hasItem(DEFAULT_MEDICAL_HISTORY)))
            .andExpect(jsonPath("$.[*].medicationDetails").value(hasItem(DEFAULT_MEDICATION_DETAILS)))
            .andExpect(jsonPath("$.[*].emergencyContact").value(hasItem(DEFAULT_EMERGENCY_CONTACT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPatientsWithEagerRelationshipsIsEnabled() throws Exception {
        when(patientRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPatientMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(patientRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPatientsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(patientRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPatientMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(patientRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPatient() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc
            .perform(get(ENTITY_API_URL_ID, patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.relationship").value(DEFAULT_RELATIONSHIP))
            .andExpect(jsonPath("$.stage").value(DEFAULT_STAGE))
            .andExpect(jsonPath("$.caregiverNotes").value(DEFAULT_CAREGIVER_NOTES))
            .andExpect(jsonPath("$.primaryConcerns").value(DEFAULT_PRIMARY_CONCERNS))
            .andExpect(jsonPath("$.medicalHistory").value(DEFAULT_MEDICAL_HISTORY))
            .andExpect(jsonPath("$.medicationDetails").value(DEFAULT_MEDICATION_DETAILS))
            .andExpect(jsonPath("$.emergencyContact").value(DEFAULT_EMERGENCY_CONTACT));
    }

    @Test
    @Transactional
    void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPatient() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the patient
        Patient updatedPatient = patientRepository.findById(patient.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPatient are not directly saved in db
        em.detach(updatedPatient);
        updatedPatient
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .age(UPDATED_AGE)
            .email(UPDATED_EMAIL)
            .gender(UPDATED_GENDER)
            .relationship(UPDATED_RELATIONSHIP)
            .stage(UPDATED_STAGE)
            .caregiverNotes(UPDATED_CAREGIVER_NOTES)
            .primaryConcerns(UPDATED_PRIMARY_CONCERNS)
            .medicalHistory(UPDATED_MEDICAL_HISTORY)
            .medicationDetails(UPDATED_MEDICATION_DETAILS)
            .emergencyContact(UPDATED_EMERGENCY_CONTACT);

        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPatient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPatientToMatchAllProperties(updatedPatient);
    }

    @Test
    @Transactional
    void putNonExistingPatient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patient.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(put(ENTITY_API_URL_ID, patient.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(patient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(patient)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatientWithPatch() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the patient using partial update
        Patient partialUpdatedPatient = new Patient();
        partialUpdatedPatient.setId(patient.getId());

        partialUpdatedPatient.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).stage(UPDATED_STAGE);

        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPatientUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPatient, patient), getPersistedPatient(patient));
    }

    @Test
    @Transactional
    void fullUpdatePatientWithPatch() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the patient using partial update
        Patient partialUpdatedPatient = new Patient();
        partialUpdatedPatient.setId(patient.getId());

        partialUpdatedPatient
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .age(UPDATED_AGE)
            .email(UPDATED_EMAIL)
            .gender(UPDATED_GENDER)
            .relationship(UPDATED_RELATIONSHIP)
            .stage(UPDATED_STAGE)
            .caregiverNotes(UPDATED_CAREGIVER_NOTES)
            .primaryConcerns(UPDATED_PRIMARY_CONCERNS)
            .medicalHistory(UPDATED_MEDICAL_HISTORY)
            .medicationDetails(UPDATED_MEDICATION_DETAILS)
            .emergencyContact(UPDATED_EMERGENCY_CONTACT);

        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPatientUpdatableFieldsEquals(partialUpdatedPatient, getPersistedPatient(partialUpdatedPatient));
    }

    @Test
    @Transactional
    void patchNonExistingPatient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patient.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patient.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(patient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(patient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        patient.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(patient)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patient in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatient() throws Exception {
        // Initialize the database
        insertedPatient = patientRepository.saveAndFlush(patient);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the patient
        restPatientMockMvc
            .perform(delete(ENTITY_API_URL_ID, patient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return patientRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Patient getPersistedPatient(Patient patient) {
        return patientRepository.findById(patient.getId()).orElseThrow();
    }

    protected void assertPersistedPatientToMatchAllProperties(Patient expectedPatient) {
        assertPatientAllPropertiesEquals(expectedPatient, getPersistedPatient(expectedPatient));
    }

    protected void assertPersistedPatientToMatchUpdatableProperties(Patient expectedPatient) {
        assertPatientAllUpdatablePropertiesEquals(expectedPatient, getPersistedPatient(expectedPatient));
    }
}
