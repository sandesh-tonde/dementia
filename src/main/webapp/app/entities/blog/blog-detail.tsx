import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './blog.reducer';

export const BlogDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const blogEntity = useAppSelector(state => state.blog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="blogDetailsHeading">
          <Translate contentKey="dementiaApp.blog.detail.title">Blog</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{blogEntity.id}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="dementiaApp.blog.content">Content</Translate>
            </span>
          </dt>
          <dd>{blogEntity.content}</dd>
          <dt>
            <span id="dateTime">
              <Translate contentKey="dementiaApp.blog.dateTime">Date Time</Translate>
            </span>
          </dt>
          <dd>{blogEntity.dateTime ? <TextFormat value={blogEntity.dateTime} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="vote">
              <Translate contentKey="dementiaApp.blog.vote">Vote</Translate>
            </span>
          </dt>
          <dd>{blogEntity.vote}</dd>
          <dt>
            <Translate contentKey="dementiaApp.blog.auther">Auther</Translate>
          </dt>
          <dd>{blogEntity.auther ? blogEntity.auther.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/blog" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/blog/${blogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BlogDetail;
