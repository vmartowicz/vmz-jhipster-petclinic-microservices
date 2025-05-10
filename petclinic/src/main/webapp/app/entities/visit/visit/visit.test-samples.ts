import dayjs from 'dayjs/esm';

import { IVisit, NewVisit } from './visit.model';

export const sampleWithRequiredData: IVisit = {
  id: 29630,
  description: 'qua ditch blindly',
};

export const sampleWithPartialData: IVisit = {
  id: 18436,
  description: 'reference',
};

export const sampleWithFullData: IVisit = {
  id: 19427,
  visitDate: dayjs('2025-01-30'),
  description: 'ouch yippee',
};

export const sampleWithNewData: NewVisit = {
  description: 'mythology worth though',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
