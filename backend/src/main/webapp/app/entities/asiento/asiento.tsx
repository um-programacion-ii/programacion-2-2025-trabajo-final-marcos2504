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

import { getEntities } from './asiento.reducer';

export const Asiento = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const asientoList = useAppSelector(state => state.asiento.entities);
  const loading = useAppSelector(state => state.asiento.loading);
  const totalItems = useAppSelector(state => state.asiento.totalItems);

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
      <h2 id="asiento-heading" data-cy="AsientoHeading">
        <Translate contentKey="eventosApp.asiento.home.title">Asientos</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="eventosApp.asiento.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/asiento/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="eventosApp.asiento.home.createLabel">Create new Asiento</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {asientoList && asientoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="eventosApp.asiento.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('fila')}>
                  <Translate contentKey="eventosApp.asiento.fila">Fila</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('fila')} />
                </th>
                <th className="hand" onClick={sort('columna')}>
                  <Translate contentKey="eventosApp.asiento.columna">Columna</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('columna')} />
                </th>
                <th className="hand" onClick={sort('estado')}>
                  <Translate contentKey="eventosApp.asiento.estado">Estado</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('estado')} />
                </th>
                <th className="hand" onClick={sort('persona')}>
                  <Translate contentKey="eventosApp.asiento.persona">Persona</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('persona')} />
                </th>
                <th className="hand" onClick={sort('bloqueadoHasta')}>
                  <Translate contentKey="eventosApp.asiento.bloqueadoHasta">Bloqueado Hasta</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('bloqueadoHasta')} />
                </th>
                <th>
                  <Translate contentKey="eventosApp.asiento.evento">Evento</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="eventosApp.asiento.venta">Venta</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {asientoList.map((asiento, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/asiento/${asiento.id}`} color="link" size="sm">
                      {asiento.id}
                    </Button>
                  </td>
                  <td>{asiento.fila}</td>
                  <td>{asiento.columna}</td>
                  <td>{asiento.estado}</td>
                  <td>{asiento.persona}</td>
                  <td>
                    {asiento.bloqueadoHasta ? <TextFormat type="date" value={asiento.bloqueadoHasta} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{asiento.evento ? <Link to={`/evento/${asiento.evento.id}`}>{asiento.evento.id}</Link> : ''}</td>
                  <td>{asiento.venta ? <Link to={`/venta/${asiento.venta.id}`}>{asiento.venta.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/asiento/${asiento.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/asiento/${asiento.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/asiento/${asiento.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="eventosApp.asiento.home.notFound">No Asientos found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={asientoList && asientoList.length > 0 ? '' : 'd-none'}>
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

export default Asiento;
