import { ISpecialty } from 'app/entities/vet/specialty/specialty.model';

export interface IVet {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  specialties?: ISpecialty[] | null;
}

export type NewVet = Omit<IVet, 'id'> & { id: null };
