import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './evento-tipo.reducer';

export const EventoTipoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const eventoTipoEntity = useAppSelector(state => state.eventoTipo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="eventoTipoDetailsHeading">
          <Translate contentKey="eventosApp.eventoTipo.detail.title">EventoTipo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{eventoTipoEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="eventosApp.eventoTipo.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{eventoTipoEntity.nombre}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="eventosApp.eventoTipo.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{eventoTipoEntity.descripcion}</dd>
        </dl>
        <Button tag={Link} to="/evento-tipo" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/evento-tipo/${eventoTipoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EventoTipoDetail;
