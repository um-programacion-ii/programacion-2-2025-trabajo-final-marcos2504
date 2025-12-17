import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './sesion.reducer';

export const Sesion = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const sesionList = useAppSelector(state => state.sesion.entities);
  const loading = useAppSelector(state => state.sesion.loading);
  const totalItems = useAppSelector(state => state.sesion.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="sesion-heading" data-cy="SesionHeading">
        <Translate contentKey="eventosApp.sesion.home.title">Sesions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="eventosApp.sesion.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/sesion/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="eventosApp.sesion.home.createLabel">Create new Sesion</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {sesionList && sesionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="eventosApp.sesion.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('tokenJWT')}>
                  <Translate contentKey="eventosApp.sesion.tokenJWT">Token JWT</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tokenJWT')} />
                </th>
                <th className="hand" onClick={sort('fechaInicio')}>
                  <Translate contentKey="eventosApp.sesion.fechaInicio">Fecha Inicio</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fechaInicio')} />
                </th>
                <th className="hand" onClick={sort('fechaExpiracion')}>
                  <Translate contentKey="eventosApp.sesion.fechaExpiracion">Fecha Expiracion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fechaExpiracion')} />
                </th>
                <th className="hand" onClick={sort('activa')}>
                  <Translate contentKey="eventosApp.sesion.activa">Activa</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('activa')} />
                </th>
                <th className="hand" onClick={sort('ultimoAcceso')}>
                  <Translate contentKey="eventosApp.sesion.ultimoAcceso">Ultimo Acceso</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ultimoAcceso')} />
                </th>
                <th className="hand" onClick={sort('eventoSeleccionado')}>
                  <Translate contentKey="eventosApp.sesion.eventoSeleccionado">Evento Seleccionado</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('eventoSeleccionado')} />
                </th>
                <th className="hand" onClick={sort('estadoSesion')}>
                  <Translate contentKey="eventosApp.sesion.estadoSesion">Estado Sesion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('estadoSesion')} />
                </th>
                <th className="hand" onClick={sort('asientosSeleccionados')}>
                  <Translate contentKey="eventosApp.sesion.asientosSeleccionados">Asientos Seleccionados</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('asientosSeleccionados')} />
                </th>
                <th className="hand" onClick={sort('cantidadAsientos')}>
                  <Translate contentKey="eventosApp.sesion.cantidadAsientos">Cantidad Asientos</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cantidadAsientos')} />
                </th>
                <th>
                  <Translate contentKey="eventosApp.sesion.usuario">Usuario</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {sesionList.map((sesion, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/sesion/${sesion.id}`} color="link" size="sm">
                      {sesion.id}
                    </Button>
                  </td>
                  <td>{sesion.tokenJWT}</td>
                  <td>{sesion.fechaInicio ? <TextFormat type="date" value={sesion.fechaInicio} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {sesion.fechaExpiracion ? <TextFormat type="date" value={sesion.fechaExpiracion} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{sesion.activa ? 'true' : 'false'}</td>
                  <td>{sesion.ultimoAcceso ? <TextFormat type="date" value={sesion.ultimoAcceso} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{sesion.eventoSeleccionado}</td>
                  <td>
                    <Translate contentKey={`eventosApp.EstadoSesion.${sesion.estadoSesion}`} />
                  </td>
                  <td>{sesion.asientosSeleccionados}</td>
                  <td>{sesion.cantidadAsientos}</td>
                  <td>{sesion.usuario ? sesion.usuario.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/sesion/${sesion.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/sesion/${sesion.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/sesion/${sesion.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="eventosApp.sesion.home.notFound">No Sesions found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={sesionList && sesionList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Sesion;
