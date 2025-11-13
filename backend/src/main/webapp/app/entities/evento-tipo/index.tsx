import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EventoTipo from './evento-tipo';
import EventoTipoDetail from './evento-tipo-detail';
import EventoTipoUpdate from './evento-tipo-update';
import EventoTipoDeleteDialog from './evento-tipo-delete-dialog';

const EventoTipoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EventoTipo />} />
    <Route path="new" element={<EventoTipoUpdate />} />
    <Route path=":id">
      <Route index element={<EventoTipoDetail />} />
      <Route path="edit" element={<EventoTipoUpdate />} />
      <Route path="delete" element={<EventoTipoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EventoTipoRoutes;
