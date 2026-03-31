import { IEvento } from 'app/shared/model/evento.model';

export interface IIntegrante {
  id?: number;
  nombre?: string;
  apellido?: string;
  identificacion?: string | null;
  evento?: IEvento | null;
}

export const defaultValue: Readonly<IIntegrante> = {};
