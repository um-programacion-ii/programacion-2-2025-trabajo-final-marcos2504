import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getEventoTipos } from 'app/entities/evento-tipo/evento-tipo.reducer';
import { createEntity, getEntity, reset, updateEntity } from './evento.reducer';

export const EventoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const eventoTipos = useAppSelector(state => state.eventoTipo.entities);
  const eventoEntity = useAppSelector(state => state.evento.entity);
  const loading = useAppSelector(state => state.evento.loading);
  const updating = useAppSelector(state => state.evento.updating);
  const updateSuccess = useAppSelector(state => state.evento.updateSuccess);

  const handleClose = () => {
    navigate(`/evento${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEventoTipos({}));
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
    values.fecha = convertDateTimeToServer(values.fecha);
    if (values.filaAsientos !== undefined && typeof values.filaAsientos !== 'number') {
      values.filaAsientos = Number(values.filaAsientos);
    }
    if (values.columnaAsientos !== undefined && typeof values.columnaAsientos !== 'number') {
      values.columnaAsientos = Number(values.columnaAsientos);
    }
    if (values.precioEntrada !== undefined && typeof values.precioEntrada !== 'number') {
      values.precioEntrada = Number(values.precioEntrada);
    }

    const entity = {
      ...eventoEntity,
      ...values,
      tipo: eventoTipos.find(it => it.id.toString() === values.tipo?.toString()),
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
          fecha: displayDefaultDateTime(),
        }
      : {
          ...eventoEntity,
          fecha: convertDateTimeFromServer(eventoEntity.fecha),
          tipo: eventoEntity?.tipo?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="eventosApp.evento.home.createOrEditLabel" data-cy="EventoCreateUpdateHeading">
            <Translate contentKey="eventosApp.evento.home.createOrEditLabel">Create or edit a Evento</Translate>
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
                  id="evento-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('eventosApp.evento.titulo')}
                id="evento-titulo"
                name="titulo"
                data-cy="titulo"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('eventosApp.evento.resumen')}
                id="evento-resumen"
                name="resumen"
                data-cy="resumen"
                type="text"
              />
              <ValidatedField
                label={translate('eventosApp.evento.descripcion')}
                id="evento-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="textarea"
              />
              <ValidatedField
                label={translate('eventosApp.evento.fecha')}
                id="evento-fecha"
                name="fecha"
                data-cy="fecha"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('eventosApp.evento.direccion')}
                id="evento-direccion"
                name="direccion"
                data-cy="direccion"
                type="text"
              />
              <ValidatedField
                label={translate('eventosApp.evento.imagen')}
                id="evento-imagen"
                name="imagen"
                data-cy="imagen"
                type="textarea"
              />
              <ValidatedField
                label={translate('eventosApp.evento.filaAsientos')}
                id="evento-filaAsientos"
                name="filaAsientos"
                data-cy="filaAsientos"
                type="text"
              />
              <ValidatedField
                label={translate('eventosApp.evento.columnaAsientos')}
                id="evento-columnaAsientos"
                name="columnaAsientos"
                data-cy="columnaAsientos"
                type="text"
              />
              <ValidatedField
                label={translate('eventosApp.evento.precioEntrada')}
                id="evento-precioEntrada"
                name="precioEntrada"
                data-cy="precioEntrada"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField id="evento-tipo" name="tipo" data-cy="tipo" label={translate('eventosApp.evento.tipo')} type="select">
                <option value="" key="0" />
                {eventoTipos
                  ? eventoTipos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/evento" replace color="info">
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

export default EventoUpdate;
