import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Sesion from './sesion';
import SesionDetail from './sesion-detail';
import SesionUpdate from './sesion-update';
import SesionDeleteDialog from './sesion-delete-dialog';

const SesionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Sesion />} />
    <Route path="new" element={<SesionUpdate />} />
    <Route path=":id">
      <Route index element={<SesionDetail />} />
      <Route path="edit" element={<SesionUpdate />} />
      <Route path="delete" element={<SesionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SesionRoutes;
