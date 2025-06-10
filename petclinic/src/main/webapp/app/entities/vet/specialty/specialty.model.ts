import { IVet } from 'app/entities/vet/vet/vet.model';

export interface ISpecialty {
  id: number;
  name?: string | null;
  vets?: Pick<IVet, 'id'>[] | null;
}

export type NewSpecialty = Omit<ISpecialty, 'id'> & { id: null };
