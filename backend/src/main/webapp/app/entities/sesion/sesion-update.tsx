import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { EstadoSesion } from 'app/shared/model/enumerations/estado-sesion.model';
import { createEntity, getEntity, reset, updateEntity } from './sesion.reducer';

export const SesionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const sesionEntity = useAppSelector(state => state.sesion.entity);
  const loading = useAppSelector(state => state.sesion.loading);
  const updating = useAppSelector(state => state.sesion.updating);
  const updateSuccess = useAppSelector(state => state.sesion.updateSuccess);
  const estadoSesionValues = Object.keys(EstadoSesion);

  const handleClose = () => {
    navigate(`/sesion${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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
    values.fechaInicio = convertDateTimeToServer(values.fechaInicio);
    values.fechaExpiracion = convertDateTimeToServer(values.fechaExpiracion);
    values.ultimoAcceso = convertDateTimeToServer(values.ultimoAcceso);
    if (values.eventoSeleccionado !== undefined && typeof values.eventoSeleccionado !== 'number') {
      values.eventoSeleccionado = Number(values.eventoSeleccionado);
    }
    if (values.cantidadAsientos !== undefined && typeof values.cantidadAsientos !== 'number') {
      values.cantidadAsientos = Number(values.cantidadAsientos);
    }

    const entity = {
      ...sesionEntity,
      ...values,
      usuario: users.find(it => it.id.toString() === values.usuario?.toString()),
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
          fechaInicio: displayDefaultDateTime(),
          fechaExpiracion: displayDefaultDateTime(),
          ultimoAcceso: displayDefaultDateTime(),
        }
      : {
          estadoSesion: 'LISTA_EVENTOS',
          ...sesionEntity,
          fechaInicio: convertDateTimeFromServer(sesionEntity.fechaInicio),
          fechaExpiracion: convertDateTimeFromServer(sesionEntity.fechaExpiracion),
          ultimoAcceso: convertDateTimeFromServer(sesionEntity.ultimoAcceso),
          usuario: sesionEntity?.usuario?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="eventosApp.sesion.home.createOrEditLabel" data-cy="SesionCreateUpdateHeading">
            <Translate contentKey="eventosApp.sesion.home.createOrEditLabel">Create or edit a Sesion</Translate>
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
                  id="sesion-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('eventosApp.sesion.tokenJWT')}
                id="sesion-tokenJWT"
                name="tokenJWT"
                data-cy="tokenJWT"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('eventosApp.sesion.fechaInicio')}
                id="sesion-fechaInicio"
                name="fechaInicio"
                data-cy="fechaInicio"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('eventosApp.sesion.fechaExpiracion')}
                id="sesion-fechaExpiracion"
                name="fechaExpiracion"
                data-cy="fechaExpiracion"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('eventosApp.sesion.activa')}
                id="sesion-activa"
                name="activa"
                data-cy="activa"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('eventosApp.sesion.ultimoAcceso')}
                id="sesion-ultimoAcceso"
                name="ultimoAcceso"
                data-cy="ultimoAcceso"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('eventosApp.sesion.eventoSeleccionado')}
                id="sesion-eventoSeleccionado"
                name="eventoSeleccionado"
                data-cy="eventoSeleccionado"
                type="text"
              />
              <ValidatedField
                label={translate('eventosApp.sesion.estadoSesion')}
                id="sesion-estadoSesion"
                name="estadoSesion"
                data-cy="estadoSesion"
                type="select"
              >
                {estadoSesionValues.map(estadoSesion => (
                  <option value={estadoSesion} key={estadoSesion}>
                    {translate(`eventosApp.EstadoSesion.${estadoSesion}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('eventosApp.sesion.asientosSeleccionados')}
                id="sesion-asientosSeleccionados"
                name="asientosSeleccionados"
                data-cy="asientosSeleccionados"
                type="textarea"
              />
              <ValidatedField
                label={translate('eventosApp.sesion.cantidadAsientos')}
                id="sesion-cantidadAsientos"
                name="cantidadAsientos"
                data-cy="cantidadAsientos"
                type="text"
              />
              <ValidatedField
                id="sesion-usuario"
                name="usuario"
                data-cy="usuario"
                label={translate('eventosApp.sesion.usuario')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/sesion" replace color="info">
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

export default SesionUpdate;
