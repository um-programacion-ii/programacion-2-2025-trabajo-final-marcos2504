export interface IEventoTipo {
  id?: number;
  nombre?: string;
  descripcion?: string | null;
}

export const defaultValue: Readonly<IEventoTipo> = {};
