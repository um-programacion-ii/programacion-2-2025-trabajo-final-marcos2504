import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Evento from './evento';
import EventoDetail from './evento-detail';
import EventoUpdate from './evento-update';
import EventoDeleteDialog from './evento-delete-dialog';

const EventoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Evento />} />
    <Route path="new" element={<EventoUpdate />} />
    <Route path=":id">
      <Route index element={<EventoDetail />} />
      <Route path="edit" element={<EventoUpdate />} />
      <Route path="delete" element={<EventoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EventoRoutes;
