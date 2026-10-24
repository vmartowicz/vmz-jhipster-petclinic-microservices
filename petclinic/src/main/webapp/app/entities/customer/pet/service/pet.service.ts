import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, microserviceContextPath, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPet, NewPet } from '../pet.model';

export type PartialUpdatePet = Partial<IPet> & Pick<IPet, 'id'>;

type RestOf<T extends IPet | NewPet> = Omit<T, 'birthDate'> & {
  birthDate?: string | null;
};

export type RestPet = RestOf<IPet>;

export type NewRestPet = RestOf<NewPet>;

export type PartialUpdateRestPet = RestOf<PartialUpdatePet>;

@Service()
export class PetsService {
  readonly petsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(undefined);
  readonly petsResource = httpResource<RestPet[]>(() => {
    const params = this.petsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of pet that have been fetched. It is updated when the petsResource emits a new value.
   * In case of error while fetching the pets, the signal is set to an empty array.
   */
  readonly pets = computed(() =>
    (this.petsResource.hasValue() ? this.petsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}${microserviceContextPath}customer/api/pets`;

  protected convertValueFromServer(restPet: RestPet): IPet {
    return {
      ...restPet,
      birthDate: restPet.birthDate ? dayjs(restPet.birthDate) : undefined,
    };
  }
}

@Service()
export class PetService extends PetsService {
  protected readonly http = inject(HttpClient);

  create(pet: NewPet): Observable<IPet> {
    const copy = this.convertValueFromClient(pet);
    return this.http.post<RestPet>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pet: IPet): Observable<IPet> {
    const copy = this.convertValueFromClient(pet);
    return this.http
      .put<RestPet>(`${this.resourceUrl}/${encodeURIComponent(this.getPetIdentifier(pet))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pet: PartialUpdatePet): Observable<IPet> {
    const copy = this.convertValueFromClient(pet);
    return this.http
      .patch<RestPet>(`${this.resourceUrl}/${encodeURIComponent(this.getPetIdentifier(pet))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IPet> {
    return this.http.get<RestPet>(`${this.resourceUrl}/${encodeURIComponent(id)}`).pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IPet[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPet[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPetIdentifier(pet: Pick<IPet, 'id'>): number {
    return pet.id;
  }

  comparePet(o1: Pick<IPet, 'id'> | null, o2: Pick<IPet, 'id'> | null): boolean {
    return o1 && o2 ? this.getPetIdentifier(o1) === this.getPetIdentifier(o2) : o1 === o2;
  }

  addPetToCollectionIfMissing<Type extends Pick<IPet, 'id'>>(petCollection: Type[], ...petsToCheck: (Type | null | undefined)[]): Type[] {
    const pets: Type[] = petsToCheck.filter(petItem => petItem !== null && petItem !== undefined);
    if (pets.length > 0) {
      const petCollectionIdentifiers = petCollection.map(petItem => this.getPetIdentifier(petItem));
      const petsToAdd = pets.filter(petItem => {
        const petIdentifier = this.getPetIdentifier(petItem);
        if (petCollectionIdentifiers.includes(petIdentifier)) {
          return false;
        }
        petCollectionIdentifiers.push(petIdentifier);
        return true;
      });
      return [...petsToAdd, ...petCollection];
    }
    return petCollection;
  }

  protected convertValueFromClient<T extends IPet | NewPet | PartialUpdatePet>(pet: T): RestOf<T> {
    return {
      ...pet,
      birthDate: pet.birthDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestPet): IPet {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestPet[]): IPet[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
