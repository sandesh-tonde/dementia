import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IPatient } from 'app/shared/model/patient.model';
import { getEntity, updateEntity, createEntity, reset } from './patient.reducer';

export const PatientUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const patientEntity = useAppSelector(state => state.patient.entity);
  const loading = useAppSelector(state => state.patient.loading);
  const updating = useAppSelector(state => state.patient.updating);
  const updateSuccess = useAppSelector(state => state.patient.updateSuccess);

  const handleClose = () => {
    navigate('/patient' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.age !== undefined && typeof values.age !== 'number') {
      values.age = Number(values.age);
    }

    const entity = {
      ...patientEntity,
      ...values,
      user_petient: users.find(it => it.id.toString() === values.user_petient?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...patientEntity,
          user_petient: patientEntity?.user_petient?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dementiaApp.patient.home.createOrEditLabel" data-cy="PatientCreateUpdateHeading">
            <Translate contentKey="dementiaApp.patient.home.createOrEditLabel">Create or edit a Patient</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="patient-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dementiaApp.patient.firstName')}
                id="patient-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('dementiaApp.patient.lastName')}
                id="patient-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('dementiaApp.patient.age')}
                id="patient-age"
                name="age"
                data-cy="age"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  max: { value: 120, message: translate('entity.validation.max', { max: 120 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('dementiaApp.patient.email')}
                id="patient-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('dementiaApp.patient.gender')}
                id="patient-gender"
                name="gender"
                data-cy="gender"
                type="text"
              />
              <ValidatedField
                label={translate('dementiaApp.patient.relationship')}
                id="patient-relationship"
                name="relationship"
                data-cy="relationship"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('dementiaApp.patient.stage')}
                id="patient-stage"
                name="stage"
                data-cy="stage"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('dementiaApp.patient.caregiverNotes')}
                id="patient-caregiverNotes"
                name="caregiverNotes"
                data-cy="caregiverNotes"
                type="text"
              />
              <ValidatedField
                label={translate('dementiaApp.patient.primaryConcerns')}
                id="patient-primaryConcerns"
                name="primaryConcerns"
                data-cy="primaryConcerns"
                type="text"
              />
              <ValidatedField
                label={translate('dementiaApp.patient.medicalHistory')}
                id="patient-medicalHistory"
                name="medicalHistory"
                data-cy="medicalHistory"
                type="text"
              />
              <ValidatedField
                label={translate('dementiaApp.patient.medicationDetails')}
                id="patient-medicationDetails"
                name="medicationDetails"
                data-cy="medicationDetails"
                type="text"
              />
              <ValidatedField
                label={translate('dementiaApp.patient.emergencyContact')}
                id="patient-emergencyContact"
                name="emergencyContact"
                data-cy="emergencyContact"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="patient-user_petient"
                name="user_petient"
                data-cy="user_petient"
                label={translate('dementiaApp.patient.user_petient')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.firstName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/patient" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PatientUpdate;
