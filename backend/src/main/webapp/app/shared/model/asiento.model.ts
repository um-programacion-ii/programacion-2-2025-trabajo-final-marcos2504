import dayjs from 'dayjs';
import { IEvento } from 'app/shared/model/evento.model';
import { IVenta } from 'app/shared/model/venta.model';
import { EstadoAsiento } from 'app/shared/model/enumerations/estado-asiento.model';

export interface IAsiento {
  id?: number;
  fila?: number;
  columna?: number;
  estadoAsiento?: keyof typeof EstadoAsiento;
  persona?: string | null;
  bloqueadoHasta?: dayjs.Dayjs | null;
  evento?: IEvento | null;
  venta?: IVenta | null;
}

export const defaultValue: Readonly<IAsiento> = {};
