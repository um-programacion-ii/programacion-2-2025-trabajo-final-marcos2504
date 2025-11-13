import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface ISesion {
  id?: number;
  tokenJWT?: string;
  fechaInicio?: dayjs.Dayjs;
  fechaExpiracion?: dayjs.Dayjs | null;
  activa?: boolean;
  ultimoAcceso?: dayjs.Dayjs | null;
  usuario?: IUser | null;
}

export const defaultValue: Readonly<ISesion> = {
  activa: false,
};
