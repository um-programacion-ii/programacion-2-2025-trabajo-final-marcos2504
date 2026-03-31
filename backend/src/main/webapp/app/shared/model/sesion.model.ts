import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { EstadoSesion } from 'app/shared/model/enumerations/estado-sesion.model';

export interface ISesion {
  id?: number;
  tokenJWT?: string;
  fechaInicio?: dayjs.Dayjs;
  fechaExpiracion?: dayjs.Dayjs | null;
  activa?: boolean;
  ultimoAcceso?: dayjs.Dayjs | null;
  eventoSeleccionado?: number | null;
  estadoSesion?: keyof typeof EstadoSesion | null;
  asientosSeleccionados?: string | null;
  cantidadAsientos?: number | null;
  usuario?: IUser | null;
}

export const defaultValue: Readonly<ISesion> = {
  activa: false,
};
