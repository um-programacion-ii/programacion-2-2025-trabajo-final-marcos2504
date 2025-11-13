import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/evento-tipo">
        <Translate contentKey="global.menu.entities.eventoTipo" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/integrante">
        <Translate contentKey="global.menu.entities.integrante" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/evento">
        <Translate contentKey="global.menu.entities.evento" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/asiento">
        <Translate contentKey="global.menu.entities.asiento" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/venta">
        <Translate contentKey="global.menu.entities.venta" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sesion">
        <Translate contentKey="global.menu.entities.sesion" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
