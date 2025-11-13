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

import { getEntities } from './evento.reducer';

export const Evento = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const eventoList = useAppSelector(state => state.evento.entities);
  const loading = useAppSelector(state => state.evento.loading);
  const totalItems = useAppSelector(state => state.evento.totalItems);

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
      <h2 id="evento-heading" data-cy="EventoHeading">
        <Translate contentKey="eventosApp.evento.home.title">Eventos</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="eventosApp.evento.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/evento/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="eventosApp.evento.home.createLabel">Create new Evento</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {eventoList && eventoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="eventosApp.evento.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('idCatedra')}>
                  <Translate contentKey="eventosApp.evento.idCatedra">Id Catedra</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('idCatedra')} />
                </th>
                <th className="hand" onClick={sort('titulo')}>
                  <Translate contentKey="eventosApp.evento.titulo">Titulo</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('titulo')} />
                </th>
                <th className="hand" onClick={sort('resumen')}>
                  <Translate contentKey="eventosApp.evento.resumen">Resumen</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('resumen')} />
                </th>
                <th className="hand" onClick={sort('descripcion')}>
                  <Translate contentKey="eventosApp.evento.descripcion">Descripcion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('descripcion')} />
                </th>
                <th className="hand" onClick={sort('fecha')}>
                  <Translate contentKey="eventosApp.evento.fecha">Fecha</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fecha')} />
                </th>
                <th className="hand" onClick={sort('direccion')}>
                  <Translate contentKey="eventosApp.evento.direccion">Direccion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('direccion')} />
                </th>
                <th className="hand" onClick={sort('imagen')}>
                  <Translate contentKey="eventosApp.evento.imagen">Imagen</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('imagen')} />
                </th>
                <th className="hand" onClick={sort('filaAsientos')}>
                  <Translate contentKey="eventosApp.evento.filaAsientos">Fila Asientos</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('filaAsientos')} />
                </th>
                <th className="hand" onClick={sort('columnaAsientos')}>
                  <Translate contentKey="eventosApp.evento.columnaAsientos">Columna Asientos</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('columnaAsientos')} />
                </th>
                <th className="hand" onClick={sort('precioEntrada')}>
                  <Translate contentKey="eventosApp.evento.precioEntrada">Precio Entrada</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('precioEntrada')} />
                </th>
                <th>
                  <Translate contentKey="eventosApp.evento.tipo">Tipo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {eventoList.map((evento, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/evento/${evento.id}`} color="link" size="sm">
                      {evento.id}
                    </Button>
                  </td>
                  <td>{evento.idCatedra}</td>
                  <td>{evento.titulo}</td>
                  <td>{evento.resumen}</td>
                  <td>{evento.descripcion}</td>
                  <td>{evento.fecha ? <TextFormat type="date" value={evento.fecha} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{evento.direccion}</td>
                  <td>{evento.imagen}</td>
                  <td>{evento.filaAsientos}</td>
                  <td>{evento.columnaAsientos}</td>
                  <td>{evento.precioEntrada}</td>
                  <td>{evento.tipo ? <Link to={`/evento-tipo/${evento.tipo.id}`}>{evento.tipo.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/evento/${evento.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/evento/${evento.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/evento/${evento.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="eventosApp.evento.home.notFound">No Eventos found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={eventoList && eventoList.length > 0 ? '' : 'd-none'}>
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

export default Evento;
