import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IEvento } from 'app/shared/model/evento.model';

export interface IVenta {
  id?: number;
  ventaIdCatedra?: number | null;
  fechaVenta?: dayjs.Dayjs;
  resultado?: boolean;
  descripcion?: string | null;
  precioVenta?: number;
  cantidadAsientos?: number | null;
  usuario?: IUser | null;
  evento?: IEvento | null;
}

export const defaultValue: Readonly<IVenta> = {
  resultado: false,
};
