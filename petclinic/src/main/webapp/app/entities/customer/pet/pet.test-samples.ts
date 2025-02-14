import dayjs from 'dayjs/esm';

import { IPet, NewPet } from './pet.model';

export const sampleWithRequiredData: IPet = {
  id: 19355,
  name: 'though scientific',
};

export const sampleWithPartialData: IPet = {
  id: 2560,
  name: 'where',
};

export const sampleWithFullData: IPet = {
  id: 10888,
  name: 'dead now',
  birthDate: dayjs('2025-01-30'),
};

export const sampleWithNewData: NewPet = {
  name: 'other so',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
