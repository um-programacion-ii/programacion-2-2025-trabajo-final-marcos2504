import eventoTipo from 'app/entities/evento-tipo/evento-tipo.reducer';
import integrante from 'app/entities/integrante/integrante.reducer';
import evento from 'app/entities/evento/evento.reducer';
import asiento from 'app/entities/asiento/asiento.reducer';
import venta from 'app/entities/venta/venta.reducer';
import sesion from 'app/entities/sesion/sesion.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  eventoTipo,
  integrante,
  evento,
  asiento,
  venta,
  sesion,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
