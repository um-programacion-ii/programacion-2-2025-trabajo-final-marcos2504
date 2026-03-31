import dayjs from 'dayjs';
import { IEventoTipo } from 'app/shared/model/evento-tipo.model';

export interface IEvento {
  id?: number;
  idCatedra?: number | null;
  titulo?: string;
  resumen?: string | null;
  descripcion?: string | null;
  fecha?: dayjs.Dayjs;
  direccion?: string | null;
  imagen?: string | null;
  filaAsientos?: number | null;
  columnaAsientos?: number | null;
  precioEntrada?: number;
  tipo?: IEventoTipo | null;
}

export const defaultValue: Readonly<IEvento> = {};
