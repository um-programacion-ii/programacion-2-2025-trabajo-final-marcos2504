import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './asiento.reducer';

export const AsientoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const asientoEntity = useAppSelector(state => state.asiento.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="asientoDetailsHeading">
          <Translate contentKey="eventosApp.asiento.detail.title">Asiento</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{asientoEntity.id}</dd>
          <dt>
            <span id="fila">
              <Translate contentKey="eventosApp.asiento.fila">Fila</Translate>
            </span>
          </dt>
          <dd>{asientoEntity.fila}</dd>
          <dt>
            <span id="columna">
              <Translate contentKey="eventosApp.asiento.columna">Columna</Translate>
            </span>
          </dt>
          <dd>{asientoEntity.columna}</dd>
          <dt>
            <span id="estado">
              <Translate contentKey="eventosApp.asiento.estado">Estado</Translate>
            </span>
          </dt>
          <dd>{asientoEntity.estado}</dd>
          <dt>
            <span id="persona">
              <Translate contentKey="eventosApp.asiento.persona">Persona</Translate>
            </span>
          </dt>
          <dd>{asientoEntity.persona}</dd>
          <dt>
            <span id="bloqueadoHasta">
              <Translate contentKey="eventosApp.asiento.bloqueadoHasta">Bloqueado Hasta</Translate>
            </span>
          </dt>
          <dd>
            {asientoEntity.bloqueadoHasta ? <TextFormat value={asientoEntity.bloqueadoHasta} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="eventosApp.asiento.evento">Evento</Translate>
          </dt>
          <dd>{asientoEntity.evento ? asientoEntity.evento.id : ''}</dd>
          <dt>
            <Translate contentKey="eventosApp.asiento.venta">Venta</Translate>
          </dt>
          <dd>{asientoEntity.venta ? asientoEntity.venta.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/asiento" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/asiento/${asientoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AsientoDetail;
