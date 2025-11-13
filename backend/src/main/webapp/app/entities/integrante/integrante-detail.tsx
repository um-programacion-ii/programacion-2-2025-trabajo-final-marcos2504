import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './integrante.reducer';

export const IntegranteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const integranteEntity = useAppSelector(state => state.integrante.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="integranteDetailsHeading">
          <Translate contentKey="eventosApp.integrante.detail.title">Integrante</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{integranteEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="eventosApp.integrante.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{integranteEntity.nombre}</dd>
          <dt>
            <span id="apellido">
              <Translate contentKey="eventosApp.integrante.apellido">Apellido</Translate>
            </span>
          </dt>
          <dd>{integranteEntity.apellido}</dd>
          <dt>
            <span id="identificacion">
              <Translate contentKey="eventosApp.integrante.identificacion">Identificacion</Translate>
            </span>
          </dt>
          <dd>{integranteEntity.identificacion}</dd>
          <dt>
            <Translate contentKey="eventosApp.integrante.evento">Evento</Translate>
          </dt>
          <dd>{integranteEntity.evento ? integranteEntity.evento.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/integrante" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/integrante/${integranteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default IntegranteDetail;
