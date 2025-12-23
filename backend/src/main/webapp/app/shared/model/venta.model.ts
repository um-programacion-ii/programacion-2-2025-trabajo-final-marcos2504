import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IEvento } from 'app/shared/model/evento.model';
import { EstadoVenta } from 'app/shared/model/enumerations/estado-venta.model';

export interface IVenta {
  id?: number;
  fechaVenta?: dayjs.Dayjs;
  resultado?: boolean;
  descripcion?: string | null;
  precioVenta?: number;
  cantidadAsientos?: number | null;
  estadoVenta?: keyof typeof EstadoVenta | null;
  usuario?: IUser | null;
  evento?: IEvento | null;
}

export const defaultValue: Readonly<IVenta> = {
  resultado: false,
};
