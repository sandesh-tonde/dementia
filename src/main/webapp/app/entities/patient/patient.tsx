import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './patient.reducer';

export const Patient = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const patientList = useAppSelector(state => state.patient.entities);
  const loading = useAppSelector(state => state.patient.loading);
  const totalItems = useAppSelector(state => state.patient.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="patient-heading" data-cy="PatientHeading">
        <Translate contentKey="dementiaApp.patient.home.title">Patients</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dementiaApp.patient.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/patient/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dementiaApp.patient.home.createLabel">Create new Patient</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {patientList && patientList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dementiaApp.patient.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('firstName')}>
                  <Translate contentKey="dementiaApp.patient.firstName">First Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('firstName')} />
                </th>
                <th className="hand" onClick={sort('lastName')}>
                  <Translate contentKey="dementiaApp.patient.lastName">Last Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastName')} />
                </th>
                <th className="hand" onClick={sort('age')}>
                  <Translate contentKey="dementiaApp.patient.age">Age</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('age')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  <Translate contentKey="dementiaApp.patient.email">Email</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th className="hand" onClick={sort('gender')}>
                  <Translate contentKey="dementiaApp.patient.gender">Gender</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('gender')} />
                </th>
                <th className="hand" onClick={sort('relationship')}>
                  <Translate contentKey="dementiaApp.patient.relationship">Relationship</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('relationship')} />
                </th>
                <th className="hand" onClick={sort('stage')}>
                  <Translate contentKey="dementiaApp.patient.stage">Stage</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('stage')} />
                </th>
                <th className="hand" onClick={sort('caregiverNotes')}>
                  <Translate contentKey="dementiaApp.patient.caregiverNotes">Caregiver Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('caregiverNotes')} />
                </th>
                <th className="hand" onClick={sort('primaryConcerns')}>
                  <Translate contentKey="dementiaApp.patient.primaryConcerns">Primary Concerns</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('primaryConcerns')} />
                </th>
                <th className="hand" onClick={sort('medicalHistory')}>
                  <Translate contentKey="dementiaApp.patient.medicalHistory">Medical History</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('medicalHistory')} />
                </th>
                <th className="hand" onClick={sort('medicationDetails')}>
                  <Translate contentKey="dementiaApp.patient.medicationDetails">Medication Details</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('medicationDetails')} />
                </th>
                <th className="hand" onClick={sort('emergencyContact')}>
                  <Translate contentKey="dementiaApp.patient.emergencyContact">Emergency Contact</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('emergencyContact')} />
                </th>
                <th>
                  <Translate contentKey="dementiaApp.patient.user_petient">User Petient</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {patientList.map((patient, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/patient/${patient.id}`} color="link" size="sm">
                      {patient.id}
                    </Button>
                  </td>
                  <td>{patient.firstName}</td>
                  <td>{patient.lastName}</td>
                  <td>{patient.age}</td>
                  <td>{patient.email}</td>
                  <td>{patient.gender}</td>
                  <td>{patient.relationship}</td>
                  <td>{patient.stage}</td>
                  <td>{patient.caregiverNotes}</td>
                  <td>{patient.primaryConcerns}</td>
                  <td>{patient.medicalHistory}</td>
                  <td>{patient.medicationDetails}</td>
                  <td>{patient.emergencyContact}</td>
                  <td>{patient.user_petient ? patient.user_petient.firstName : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/patient/${patient.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/patient/${patient.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/patient/${patient.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="dementiaApp.patient.home.notFound">No Patients found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={patientList && patientList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Patient;
