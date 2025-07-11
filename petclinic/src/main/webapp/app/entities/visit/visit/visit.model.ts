import dayjs from 'dayjs/esm';

export interface IVisit {
  id: number;
  visitDate?: dayjs.Dayjs | null;
  description?: string | null;
  petId?: number | null;
  petName?: string | null;
}

export type NewVisit = Omit<IVisit, 'id'> & { id: null };
