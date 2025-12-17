import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sesion.reducer';

export const SesionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const sesionEntity = useAppSelector(state => state.sesion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sesionDetailsHeading">
          <Translate contentKey="eventosApp.sesion.detail.title">Sesion</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{sesionEntity.id}</dd>
          <dt>
            <span id="tokenJWT">
              <Translate contentKey="eventosApp.sesion.tokenJWT">Token JWT</Translate>
            </span>
          </dt>
          <dd>{sesionEntity.tokenJWT}</dd>
          <dt>
            <span id="fechaInicio">
              <Translate contentKey="eventosApp.sesion.fechaInicio">Fecha Inicio</Translate>
            </span>
          </dt>
          <dd>{sesionEntity.fechaInicio ? <TextFormat value={sesionEntity.fechaInicio} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="fechaExpiracion">
              <Translate contentKey="eventosApp.sesion.fechaExpiracion">Fecha Expiracion</Translate>
            </span>
          </dt>
          <dd>
            {sesionEntity.fechaExpiracion ? <TextFormat value={sesionEntity.fechaExpiracion} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="activa">
              <Translate contentKey="eventosApp.sesion.activa">Activa</Translate>
            </span>
          </dt>
          <dd>{sesionEntity.activa ? 'true' : 'false'}</dd>
          <dt>
            <span id="ultimoAcceso">
              <Translate contentKey="eventosApp.sesion.ultimoAcceso">Ultimo Acceso</Translate>
            </span>
          </dt>
          <dd>
            {sesionEntity.ultimoAcceso ? <TextFormat value={sesionEntity.ultimoAcceso} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="eventoSeleccionado">
              <Translate contentKey="eventosApp.sesion.eventoSeleccionado">Evento Seleccionado</Translate>
            </span>
          </dt>
          <dd>{sesionEntity.eventoSeleccionado}</dd>
          <dt>
            <span id="estadoSesion">
              <Translate contentKey="eventosApp.sesion.estadoSesion">Estado Sesion</Translate>
            </span>
          </dt>
          <dd>{sesionEntity.estadoSesion}</dd>
          <dt>
            <span id="asientosSeleccionados">
              <Translate contentKey="eventosApp.sesion.asientosSeleccionados">Asientos Seleccionados</Translate>
            </span>
          </dt>
          <dd>{sesionEntity.asientosSeleccionados}</dd>
          <dt>
            <span id="cantidadAsientos">
              <Translate contentKey="eventosApp.sesion.cantidadAsientos">Cantidad Asientos</Translate>
            </span>
          </dt>
          <dd>{sesionEntity.cantidadAsientos}</dd>
          <dt>
            <Translate contentKey="eventosApp.sesion.usuario">Usuario</Translate>
          </dt>
          <dd>{sesionEntity.usuario ? sesionEntity.usuario.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/sesion" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sesion/${sesionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SesionDetail;
