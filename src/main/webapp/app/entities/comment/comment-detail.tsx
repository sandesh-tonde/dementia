import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './comment.reducer';

export const CommentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const commentEntity = useAppSelector(state => state.comment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="commentDetailsHeading">
          <Translate contentKey="dementiaApp.comment.detail.title">Comment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{commentEntity.id}</dd>
          <dt>
            <span id="commentText">
              <Translate contentKey="dementiaApp.comment.commentText">Comment Text</Translate>
            </span>
          </dt>
          <dd>{commentEntity.commentText}</dd>
          <dt>
            <span id="dateTime">
              <Translate contentKey="dementiaApp.comment.dateTime">Date Time</Translate>
            </span>
          </dt>
          <dd>
            {commentEntity.dateTime ? <TextFormat value={commentEntity.dateTime} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="dementiaApp.comment.addedBy">Added By</Translate>
          </dt>
          <dd>{commentEntity.addedBy ? commentEntity.addedBy.login : ''}</dd>
          <dt>
            <Translate contentKey="dementiaApp.comment.blog_comments">Blog Comments</Translate>
          </dt>
          <dd>{commentEntity.blog_comments ? commentEntity.blog_comments.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/comment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/comment/${commentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CommentDetail;
