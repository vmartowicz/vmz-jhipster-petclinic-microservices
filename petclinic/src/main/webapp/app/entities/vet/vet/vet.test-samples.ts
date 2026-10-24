import { IVet, NewVet } from './vet.model';

export const sampleWithRequiredData: IVet = {
  id: 2514,
  firstName: 'Nathan',
  lastName: 'Pouros',
};

export const sampleWithPartialData: IVet = {
  id: 10871,
  firstName: 'Lessie',
  lastName: 'Pacocha',
};

export const sampleWithFullData: IVet = {
  id: 29795,
  firstName: 'Hubert',
  lastName: 'Reinger',
};

export const sampleWithNewData: NewVet = {
  firstName: 'Joana',
  lastName: 'McClure',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
