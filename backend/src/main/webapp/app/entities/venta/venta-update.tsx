import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getEventos } from 'app/entities/evento/evento.reducer';
import { EstadoVenta } from 'app/shared/model/enumerations/estado-venta.model';
import { createEntity, getEntity, reset, updateEntity } from './venta.reducer';

export const VentaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const eventos = useAppSelector(state => state.evento.entities);
  const ventaEntity = useAppSelector(state => state.venta.entity);
  const loading = useAppSelector(state => state.venta.loading);
  const updating = useAppSelector(state => state.venta.updating);
  const updateSuccess = useAppSelector(state => state.venta.updateSuccess);
  const estadoVentaValues = Object.keys(EstadoVenta);

  const handleClose = () => {
    navigate(`/venta${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getEventos({}));
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
    if (values.ventaIdCatedra !== undefined && typeof values.ventaIdCatedra !== 'number') {
      values.ventaIdCatedra = Number(values.ventaIdCatedra);
    }
    values.fechaVenta = convertDateTimeToServer(values.fechaVenta);
    if (values.precioVenta !== undefined && typeof values.precioVenta !== 'number') {
      values.precioVenta = Number(values.precioVenta);
    }
    if (values.cantidadAsientos !== undefined && typeof values.cantidadAsientos !== 'number') {
      values.cantidadAsientos = Number(values.cantidadAsientos);
    }

    const entity = {
      ...ventaEntity,
      ...values,
      usuario: users.find(it => it.id.toString() === values.usuario?.toString()),
      evento: eventos.find(it => it.id.toString() === values.evento?.toString()),
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
          fechaVenta: displayDefaultDateTime(),
        }
      : {
          estadoVenta: 'PENDIENTE',
          ...ventaEntity,
          fechaVenta: convertDateTimeFromServer(ventaEntity.fechaVenta),
          usuario: ventaEntity?.usuario?.id,
          evento: ventaEntity?.evento?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="eventosApp.venta.home.createOrEditLabel" data-cy="VentaCreateUpdateHeading">
            <Translate contentKey="eventosApp.venta.home.createOrEditLabel">Create or edit a Venta</Translate>
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
                  id="venta-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('eventosApp.venta.ventaIdCatedra')}
                id="venta-ventaIdCatedra"
                name="ventaIdCatedra"
                data-cy="ventaIdCatedra"
                type="text"
              />
              <ValidatedField
                label={translate('eventosApp.venta.fechaVenta')}
                id="venta-fechaVenta"
                name="fechaVenta"
                data-cy="fechaVenta"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('eventosApp.venta.resultado')}
                id="venta-resultado"
                name="resultado"
                data-cy="resultado"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('eventosApp.venta.descripcion')}
                id="venta-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
              />
              <ValidatedField
                label={translate('eventosApp.venta.precioVenta')}
                id="venta-precioVenta"
                name="precioVenta"
                data-cy="precioVenta"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('eventosApp.venta.cantidadAsientos')}
                id="venta-cantidadAsientos"
                name="cantidadAsientos"
                data-cy="cantidadAsientos"
                type="text"
              />
              <ValidatedField
                label={translate('eventosApp.venta.estadoVenta')}
                id="venta-estadoVenta"
                name="estadoVenta"
                data-cy="estadoVenta"
                type="select"
              >
                {estadoVentaValues.map(estadoVenta => (
                  <option value={estadoVenta} key={estadoVenta}>
                    {translate(`eventosApp.EstadoVenta.${estadoVenta}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="venta-usuario"
                name="usuario"
                data-cy="usuario"
                label={translate('eventosApp.venta.usuario')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="venta-evento" name="evento" data-cy="evento" label={translate('eventosApp.venta.evento')} type="select">
                <option value="" key="0" />
                {eventos
                  ? eventos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/venta" replace color="info">
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

export default VentaUpdate;
