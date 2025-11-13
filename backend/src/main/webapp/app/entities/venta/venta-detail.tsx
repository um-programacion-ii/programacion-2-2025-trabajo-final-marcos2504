import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './venta.reducer';

export const VentaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ventaEntity = useAppSelector(state => state.venta.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ventaDetailsHeading">
          <Translate contentKey="eventosApp.venta.detail.title">Venta</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ventaEntity.id}</dd>
          <dt>
            <span id="ventaIdCatedra">
              <Translate contentKey="eventosApp.venta.ventaIdCatedra">Venta Id Catedra</Translate>
            </span>
          </dt>
          <dd>{ventaEntity.ventaIdCatedra}</dd>
          <dt>
            <span id="fechaVenta">
              <Translate contentKey="eventosApp.venta.fechaVenta">Fecha Venta</Translate>
            </span>
          </dt>
          <dd>{ventaEntity.fechaVenta ? <TextFormat value={ventaEntity.fechaVenta} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="resultado">
              <Translate contentKey="eventosApp.venta.resultado">Resultado</Translate>
            </span>
          </dt>
          <dd>{ventaEntity.resultado ? 'true' : 'false'}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="eventosApp.venta.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{ventaEntity.descripcion}</dd>
          <dt>
            <span id="precioVenta">
              <Translate contentKey="eventosApp.venta.precioVenta">Precio Venta</Translate>
            </span>
          </dt>
          <dd>{ventaEntity.precioVenta}</dd>
          <dt>
            <span id="cantidadAsientos">
              <Translate contentKey="eventosApp.venta.cantidadAsientos">Cantidad Asientos</Translate>
            </span>
          </dt>
          <dd>{ventaEntity.cantidadAsientos}</dd>
          <dt>
            <Translate contentKey="eventosApp.venta.usuario">Usuario</Translate>
          </dt>
          <dd>{ventaEntity.usuario ? ventaEntity.usuario.login : ''}</dd>
          <dt>
            <Translate contentKey="eventosApp.venta.evento">Evento</Translate>
          </dt>
          <dd>{ventaEntity.evento ? ventaEntity.evento.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/venta" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/venta/${ventaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VentaDetail;
