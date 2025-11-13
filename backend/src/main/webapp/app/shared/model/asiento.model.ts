import dayjs from 'dayjs';
import { IEvento } from 'app/shared/model/evento.model';
import { IVenta } from 'app/shared/model/venta.model';

export interface IAsiento {
  id?: number;
  fila?: number;
  columna?: number;
  estado?: string;
  persona?: string | null;
  bloqueadoHasta?: dayjs.Dayjs | null;
  evento?: IEvento | null;
  venta?: IVenta | null;
}

export const defaultValue: Readonly<IAsiento> = {};
