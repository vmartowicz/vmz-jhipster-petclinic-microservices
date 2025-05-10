import { IVet } from 'app/entities/vet/vet/vet.model';

export interface ISpecialty {
  id: number;
  name?: string | null;
  vets?: IVet[] | null;
}

export type NewSpecialty = Omit<ISpecialty, 'id'> & { id: null };
