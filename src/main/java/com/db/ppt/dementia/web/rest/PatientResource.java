package com.db.ppt.dementia.web.rest;

import com.db.ppt.dementia.domain.Patient;
import com.db.ppt.dementia.repository.PatientRepository;
import com.db.ppt.dementia.repository.UserRepository;
import com.db.ppt.dementia.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.db.ppt.dementia.domain.Patient}.
 */
@RestController
@RequestMapping("/api/patients")
@Transactional
public class PatientResource {

    private static final Logger log = LoggerFactory.getLogger(PatientResource.class);

    private static final String ENTITY_NAME = "patient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatientRepository patientRepository;

    private final UserRepository userRepository;

    public PatientResource(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /patients} : Create a new patient.
     *
     * @param patient the patient to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patient, or with status {@code 400 (Bad Request)} if the patient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) throws URISyntaxException {
        log.debug("REST request to save Patient : {}", patient);
        if (patient.getId() != null) {
            throw new BadRequestAlertException("A new patient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(patient.getUser_petient())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        Long userId = patient.getUser_petient().getId();
        userRepository.findById(userId).ifPresent(patient::user_petient);
        patient = patientRepository.save(patient);
        return ResponseEntity.created(new URI("/api/patients/" + patient.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, patient.getId().toString()))
            .body(patient);
    }

    /**
     * {@code PUT  /patients/:id} : Updates an existing patient.
     *
     * @param id the id of the patient to save.
     * @param patient the patient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patient,
     * or with status {@code 400 (Bad Request)} if the patient is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Patient patient
    ) throws URISyntaxException {
        log.debug("REST request to update Patient : {}, {}", id, patient);
        if (patient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        patient = patientRepository.save(patient);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patient.getId().toString()))
            .body(patient);
    }

    /**
     * {@code PATCH  /patients/:id} : Partial updates given fields of an existing patient, field will ignore if it is null
     *
     * @param id the id of the patient to save.
     * @param patient the patient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patient,
     * or with status {@code 400 (Bad Request)} if the patient is not valid,
     * or with status {@code 404 (Not Found)} if the patient is not found,
     * or with status {@code 500 (Internal Server Error)} if the patient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Patient> partialUpdatePatient(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Patient patient
    ) throws URISyntaxException {
        log.debug("REST request to partial update Patient partially : {}, {}", id, patient);
        if (patient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Patient> result = patientRepository
            .findById(patient.getId())
            .map(existingPatient -> {
                if (patient.getFirstName() != null) {
                    existingPatient.setFirstName(patient.getFirstName());
                }
                if (patient.getLastName() != null) {
                    existingPatient.setLastName(patient.getLastName());
                }
                if (patient.getAge() != null) {
                    existingPatient.setAge(patient.getAge());
                }
                if (patient.getEmail() != null) {
                    existingPatient.setEmail(patient.getEmail());
                }
                if (patient.getGender() != null) {
                    existingPatient.setGender(patient.getGender());
                }
                if (patient.getRelationship() != null) {
                    existingPatient.setRelationship(patient.getRelationship());
                }
                if (patient.getStage() != null) {
                    existingPatient.setStage(patient.getStage());
                }
                if (patient.getCaregiverNotes() != null) {
                    existingPatient.setCaregiverNotes(patient.getCaregiverNotes());
                }
                if (patient.getPrimaryConcerns() != null) {
                    existingPatient.setPrimaryConcerns(patient.getPrimaryConcerns());
                }
                if (patient.getMedicalHistory() != null) {
                    existingPatient.setMedicalHistory(patient.getMedicalHistory());
                }
                if (patient.getMedicationDetails() != null) {
                    existingPatient.setMedicationDetails(patient.getMedicationDetails());
                }
                if (patient.getEmergencyContact() != null) {
                    existingPatient.setEmergencyContact(patient.getEmergencyContact());
                }

                return existingPatient;
            })
            .map(patientRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patient.getId().toString())
        );
    }

    /**
     * {@code GET  /patients} : get all the patients.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patients in body.
     */
    @GetMapping("")
    @Transactional(readOnly = true)
    public ResponseEntity<List<Patient>> getAllPatients(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Patients");
        Page<Patient> page;
        if (eagerload) {
            page = patientRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = patientRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /patients/:id} : get the "id" patient.
     *
     * @param id the id of the patient to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patient, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<Patient> getPatient(@PathVariable("id") Long id) {
        log.debug("REST request to get Patient : {}", id);
        Optional<Patient> patient = patientRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(patient);
    }

    /**
     * {@code DELETE  /patients/:id} : delete the "id" patient.
     *
     * @param id the id of the patient to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") Long id) {
        log.debug("REST request to delete Patient : {}", id);
        patientRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
