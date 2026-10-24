import { ISpecialty } from 'app/entities/vet/specialty/specialty.model';

export interface IVet {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  specialtieses?: Pick<ISpecialty, 'id' | 'name'>[] | null;
}

export type NewVet = Omit<IVet, 'id'> & { id: null };
