package com.db.ppt.dementia.domain;

import static com.db.ppt.dementia.domain.PatientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.db.ppt.dementia.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PatientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Patient.class);
        Patient patient1 = getPatientSample1();
        Patient patient2 = new Patient();
        assertThat(patient1).isNotEqualTo(patient2);

        patient2.setId(patient1.getId());
        assertThat(patient1).isEqualTo(patient2);

        patient2 = getPatientSample2();
        assertThat(patient1).isNotEqualTo(patient2);
    }
}
