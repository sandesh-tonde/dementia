import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './patient.reducer';

export const PatientDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const patientEntity = useAppSelector(state => state.patient.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="patientDetailsHeading">
          <Translate contentKey="dementiaApp.patient.detail.title">Patient</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{patientEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="dementiaApp.patient.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{patientEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="dementiaApp.patient.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{patientEntity.lastName}</dd>
          <dt>
            <span id="age">
              <Translate contentKey="dementiaApp.patient.age">Age</Translate>
            </span>
          </dt>
          <dd>{patientEntity.age}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="dementiaApp.patient.email">Email</Translate>
            </span>
          </dt>
          <dd>{patientEntity.email}</dd>
          <dt>
            <span id="gender">
              <Translate contentKey="dementiaApp.patient.gender">Gender</Translate>
            </span>
          </dt>
          <dd>{patientEntity.gender}</dd>
          <dt>
            <span id="relationship">
              <Translate contentKey="dementiaApp.patient.relationship">Relationship</Translate>
            </span>
          </dt>
          <dd>{patientEntity.relationship}</dd>
          <dt>
            <span id="stage">
              <Translate contentKey="dementiaApp.patient.stage">Stage</Translate>
            </span>
          </dt>
          <dd>{patientEntity.stage}</dd>
          <dt>
            <span id="caregiverNotes">
              <Translate contentKey="dementiaApp.patient.caregiverNotes">Caregiver Notes</Translate>
            </span>
          </dt>
          <dd>{patientEntity.caregiverNotes}</dd>
          <dt>
            <span id="primaryConcerns">
              <Translate contentKey="dementiaApp.patient.primaryConcerns">Primary Concerns</Translate>
            </span>
          </dt>
          <dd>{patientEntity.primaryConcerns}</dd>
          <dt>
            <span id="medicalHistory">
              <Translate contentKey="dementiaApp.patient.medicalHistory">Medical History</Translate>
            </span>
          </dt>
          <dd>{patientEntity.medicalHistory}</dd>
          <dt>
            <span id="medicationDetails">
              <Translate contentKey="dementiaApp.patient.medicationDetails">Medication Details</Translate>
            </span>
          </dt>
          <dd>{patientEntity.medicationDetails}</dd>
          <dt>
            <span id="emergencyContact">
              <Translate contentKey="dementiaApp.patient.emergencyContact">Emergency Contact</Translate>
            </span>
          </dt>
          <dd>{patientEntity.emergencyContact}</dd>
          <dt>
            <Translate contentKey="dementiaApp.patient.user_petient">User Petient</Translate>
          </dt>
          <dd>{patientEntity.user_petient ? patientEntity.user_petient.firstName : ''}</dd>
        </dl>
        <Button tag={Link} to="/patient" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/patient/${patientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PatientDetail;
