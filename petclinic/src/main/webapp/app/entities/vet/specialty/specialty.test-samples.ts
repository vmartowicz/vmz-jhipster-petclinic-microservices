import { ISpecialty, NewSpecialty } from './specialty.model';

export const sampleWithRequiredData: ISpecialty = {
  id: 29677,
  name: 'hence',
};

export const sampleWithPartialData: ISpecialty = {
  id: 8301,
  name: 'ouch',
};

export const sampleWithFullData: ISpecialty = {
  id: 13590,
  name: 'except',
};

export const sampleWithNewData: NewSpecialty = {
  name: 'eek while yet',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
