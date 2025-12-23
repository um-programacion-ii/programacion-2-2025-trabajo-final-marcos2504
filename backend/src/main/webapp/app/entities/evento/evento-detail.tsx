import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './evento.reducer';

export const EventoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const eventoEntity = useAppSelector(state => state.evento.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="eventoDetailsHeading">
          <Translate contentKey="eventosApp.evento.detail.title">Evento</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{eventoEntity.id}</dd>
          <dt>
            <span id="titulo">
              <Translate contentKey="eventosApp.evento.titulo">Titulo</Translate>
            </span>
          </dt>
          <dd>{eventoEntity.titulo}</dd>
          <dt>
            <span id="resumen">
              <Translate contentKey="eventosApp.evento.resumen">Resumen</Translate>
            </span>
          </dt>
          <dd>{eventoEntity.resumen}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="eventosApp.evento.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{eventoEntity.descripcion}</dd>
          <dt>
            <span id="fecha">
              <Translate contentKey="eventosApp.evento.fecha">Fecha</Translate>
            </span>
          </dt>
          <dd>{eventoEntity.fecha ? <TextFormat value={eventoEntity.fecha} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="direccion">
              <Translate contentKey="eventosApp.evento.direccion">Direccion</Translate>
            </span>
          </dt>
          <dd>{eventoEntity.direccion}</dd>
          <dt>
            <span id="imagen">
              <Translate contentKey="eventosApp.evento.imagen">Imagen</Translate>
            </span>
          </dt>
          <dd>{eventoEntity.imagen}</dd>
          <dt>
            <span id="filaAsientos">
              <Translate contentKey="eventosApp.evento.filaAsientos">Fila Asientos</Translate>
            </span>
          </dt>
          <dd>{eventoEntity.filaAsientos}</dd>
          <dt>
            <span id="columnaAsientos">
              <Translate contentKey="eventosApp.evento.columnaAsientos">Columna Asientos</Translate>
            </span>
          </dt>
          <dd>{eventoEntity.columnaAsientos}</dd>
          <dt>
            <span id="precioEntrada">
              <Translate contentKey="eventosApp.evento.precioEntrada">Precio Entrada</Translate>
            </span>
          </dt>
          <dd>{eventoEntity.precioEntrada}</dd>
          <dt>
            <Translate contentKey="eventosApp.evento.tipo">Tipo</Translate>
          </dt>
          <dd>{eventoEntity.tipo ? eventoEntity.tipo.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/evento" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/evento/${eventoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EventoDetail;
