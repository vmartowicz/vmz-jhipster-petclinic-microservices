import dayjs from 'dayjs/esm';
import { IPetType } from 'app/entities/customer/pet-type/pet-type.model';
import { IOwner } from 'app/entities/customer/owner/owner.model';

export interface IPet {
  id: number;
  name?: string | null;
  birthDate?: dayjs.Dayjs | null;
  type?: IPetType | null;
  owner?: IOwner | null;
}

export type NewPet = Omit<IPet, 'id'> & { id: null };
