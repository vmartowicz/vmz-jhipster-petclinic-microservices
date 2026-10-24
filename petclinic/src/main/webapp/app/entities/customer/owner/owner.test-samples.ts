import { IOwner, NewOwner } from './owner.model';

export const sampleWithRequiredData: IOwner = {
  id: 3581,
  firstName: 'Gregoria',
  lastName: 'Erdman',
  address: 'obediently',
  city: 'Charlottesville',
  telephone: '1-830-890-2979 x7402',
};

export const sampleWithPartialData: IOwner = {
  id: 12097,
  firstName: 'Scarlett',
  lastName: 'Prosacco',
  address: 'ick huzzah delectable',
  city: 'Port Alexieborough',
  telephone: '412-580-9810 x52299',
};

export const sampleWithFullData: IOwner = {
  id: 26949,
  firstName: 'August',
  lastName: 'Barton',
  address: 'honestly when evenly',
  city: 'Pocatello',
  telephone: '1-422-319-2578 x483',
};

export const sampleWithNewData: NewOwner = {
  firstName: 'Mae',
  lastName: 'Shanahan',
  address: 'consequently youthfully',
  city: 'Fort Yasmine',
  telephone: '485-389-3348 x278',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
