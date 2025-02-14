import { IVet, NewVet } from './vet.model';

export const sampleWithRequiredData: IVet = {
  id: 2514,
  firstName: 'Zena',
  lastName: 'Reichert',
};

export const sampleWithPartialData: IVet = {
  id: 10871,
  firstName: 'Aliya',
  lastName: 'McKenzie',
};

export const sampleWithFullData: IVet = {
  id: 29795,
  firstName: 'Willy',
  lastName: 'Koch',
};

export const sampleWithNewData: NewVet = {
  firstName: 'Jada',
  lastName: 'Koss',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
