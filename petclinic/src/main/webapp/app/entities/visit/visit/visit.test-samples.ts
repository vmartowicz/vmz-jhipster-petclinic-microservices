import dayjs from 'dayjs/esm';

import { IVisit, NewVisit } from './visit.model';

export const sampleWithRequiredData: IVisit = {
  id: 29630,
  description: 'qua ditch blindly',
  petId: 25101,
};

export const sampleWithPartialData: IVisit = {
  id: 18436,
  description: 'reference',
  petId: 3431,
};

export const sampleWithFullData: IVisit = {
  id: 19427,
  visitDate: dayjs('2025-01-30'),
  description: 'ouch yippee',
  petId: 27043,
};

export const sampleWithNewData: NewVisit = {
  description: 'mythology worth though',
  petId: 17118,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
