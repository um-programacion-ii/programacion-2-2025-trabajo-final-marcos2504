import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getEventos } from 'app/entities/evento/evento.reducer';
import { getEntities as getVentas } from 'app/entities/venta/venta.reducer';
import { createEntity, getEntity, reset, updateEntity } from './asiento.reducer';

export const AsientoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const eventos = useAppSelector(state => state.evento.entities);
  const ventas = useAppSelector(state => state.venta.entities);
  const asientoEntity = useAppSelector(state => state.asiento.entity);
  const loading = useAppSelector(state => state.asiento.loading);
  const updating = useAppSelector(state => state.asiento.updating);
  const updateSuccess = useAppSelector(state => state.asiento.updateSuccess);

  const handleClose = () => {
    navigate(`/asiento${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEventos({}));
    dispatch(getVentas({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.fila !== undefined && typeof values.fila !== 'number') {
      values.fila = Number(values.fila);
    }
    if (values.columna !== undefined && typeof values.columna !== 'number') {
      values.columna = Number(values.columna);
    }
    values.bloqueadoHasta = convertDateTimeToServer(values.bloqueadoHasta);

    const entity = {
      ...asientoEntity,
      ...values,
      evento: eventos.find(it => it.id.toString() === values.evento?.toString()),
      venta: ventas.find(it => it.id.toString() === values.venta?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          bloqueadoHasta: displayDefaultDateTime(),
        }
      : {
          ...asientoEntity,
          bloqueadoHasta: convertDateTimeFromServer(asientoEntity.bloqueadoHasta),
          evento: asientoEntity?.evento?.id,
          venta: asientoEntity?.venta?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="eventosApp.asiento.home.createOrEditLabel" data-cy="AsientoCreateUpdateHeading">
            <Translate contentKey="eventosApp.asiento.home.createOrEditLabel">Create or edit a Asiento</Translate>
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
                  id="asiento-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('eventosApp.asiento.fila')}
                id="asiento-fila"
                name="fila"
                data-cy="fila"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('eventosApp.asiento.columna')}
                id="asiento-columna"
                name="columna"
                data-cy="columna"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('eventosApp.asiento.estado')}
                id="asiento-estado"
                name="estado"
                data-cy="estado"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('eventosApp.asiento.persona')}
                id="asiento-persona"
                name="persona"
                data-cy="persona"
                type="text"
              />
              <ValidatedField
                label={translate('eventosApp.asiento.bloqueadoHasta')}
                id="asiento-bloqueadoHasta"
                name="bloqueadoHasta"
                data-cy="bloqueadoHasta"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="asiento-evento"
                name="evento"
                data-cy="evento"
                label={translate('eventosApp.asiento.evento')}
                type="select"
              >
                <option value="" key="0" />
                {eventos
                  ? eventos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="asiento-venta" name="venta" data-cy="venta" label={translate('eventosApp.asiento.venta')} type="select">
                <option value="" key="0" />
                {ventas
                  ? ventas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/asiento" replace color="info">
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

export default AsientoUpdate;
