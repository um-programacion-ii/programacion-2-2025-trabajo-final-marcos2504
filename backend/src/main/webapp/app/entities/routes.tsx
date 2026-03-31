import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EventoTipo from './evento-tipo';
import Integrante from './integrante';
import Evento from './evento';
import Asiento from './asiento';
import Venta from './venta';
import Sesion from './sesion';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="evento-tipo/*" element={<EventoTipo />} />
        <Route path="integrante/*" element={<Integrante />} />
        <Route path="evento/*" element={<Evento />} />
        <Route path="asiento/*" element={<Asiento />} />
        <Route path="venta/*" element={<Venta />} />
        <Route path="sesion/*" element={<Sesion />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
