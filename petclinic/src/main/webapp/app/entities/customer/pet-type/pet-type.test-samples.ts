import { IPetType, NewPetType } from './pet-type.model';

export const sampleWithRequiredData: IPetType = {
  id: 29859,
  name: 'amid',
};

export const sampleWithPartialData: IPetType = {
  id: 16512,
  name: 'inasmuch entire',
};

export const sampleWithFullData: IPetType = {
  id: 22043,
  name: 'or blacken jealous',
};

export const sampleWithNewData: NewPetType = {
  name: 'gah incidentally',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
