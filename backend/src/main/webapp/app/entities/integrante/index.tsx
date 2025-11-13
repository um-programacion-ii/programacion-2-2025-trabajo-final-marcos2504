import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Integrante from './integrante';
import IntegranteDetail from './integrante-detail';
import IntegranteUpdate from './integrante-update';
import IntegranteDeleteDialog from './integrante-delete-dialog';

const IntegranteRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Integrante />} />
    <Route path="new" element={<IntegranteUpdate />} />
    <Route path=":id">
      <Route index element={<IntegranteDetail />} />
      <Route path="edit" element={<IntegranteUpdate />} />
      <Route path="delete" element={<IntegranteDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IntegranteRoutes;
