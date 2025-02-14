import { IOwner, NewOwner } from './owner.model';

export const sampleWithRequiredData: IOwner = {
  id: 3581,
  firstName: 'Bertrand',
  lastName: 'Hoeger',
  address: 'couch',
  city: 'North Kacieside',
  telephone: '1-983-289-0297',
};

export const sampleWithPartialData: IOwner = {
  id: 12097,
  firstName: 'Gerardo',
  lastName: 'Swaniawski',
  address: 'braid dreamily',
  city: 'Mableville',
  telephone: '959.871.0739',
};

export const sampleWithFullData: IOwner = {
  id: 26949,
  firstName: 'Janice',
  lastName: 'Brakus',
  address: 'readily',
  city: 'Haagborough',
  telephone: '920.647.6559',
};

export const sampleWithNewData: NewOwner = {
  firstName: 'Dallin',
  lastName: "O'Kon",
  address: 'despite',
  city: 'Lake Camronstead',
  telephone: '1-277-592-9009 x1385',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
