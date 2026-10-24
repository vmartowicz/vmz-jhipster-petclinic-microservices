import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { microserviceContextPath, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPetType, NewPetType } from '../pet-type.model';

export type PartialUpdatePetType = Partial<IPetType> & Pick<IPetType, 'id'>;

@Service()
export class PetTypesService {
  readonly petTypesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly petTypesResource = httpResource<IPetType[]>(() => {
    const params = this.petTypesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of petType that have been fetched. It is updated when the petTypesResource emits a new value.
   * In case of error while fetching the petTypes, the signal is set to an empty array.
   */
  readonly petTypes = computed(() => (this.petTypesResource.hasValue() ? this.petTypesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}${microserviceContextPath}customer/api/pet-types`;
}

@Service()
export class PetTypeService extends PetTypesService {
  protected readonly http = inject(HttpClient);

  create(petType: NewPetType): Observable<IPetType> {
    return this.http.post<IPetType>(this.resourceUrl, petType);
  }

  update(petType: IPetType): Observable<IPetType> {
    return this.http.put<IPetType>(`${this.resourceUrl}/${encodeURIComponent(this.getPetTypeIdentifier(petType))}`, petType);
  }

  partialUpdate(petType: PartialUpdatePetType): Observable<IPetType> {
    return this.http.patch<IPetType>(`${this.resourceUrl}/${encodeURIComponent(this.getPetTypeIdentifier(petType))}`, petType);
  }

  find(id: number): Observable<IPetType> {
    return this.http.get<IPetType>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPetType[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPetType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPetTypeIdentifier(petType: Pick<IPetType, 'id'>): number {
    return petType.id;
  }

  comparePetType(o1: Pick<IPetType, 'id'> | null, o2: Pick<IPetType, 'id'> | null): boolean {
    return o1 && o2 ? this.getPetTypeIdentifier(o1) === this.getPetTypeIdentifier(o2) : o1 === o2;
  }

  addPetTypeToCollectionIfMissing<Type extends Pick<IPetType, 'id'>>(
    petTypeCollection: Type[],
    ...petTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const petTypes: Type[] = petTypesToCheck.filter(petTypeItem => petTypeItem !== null && petTypeItem !== undefined);
    if (petTypes.length > 0) {
      const petTypeCollectionIdentifiers = petTypeCollection.map(petTypeItem => this.getPetTypeIdentifier(petTypeItem));
      const petTypesToAdd = petTypes.filter(petTypeItem => {
        const petTypeIdentifier = this.getPetTypeIdentifier(petTypeItem);
        if (petTypeCollectionIdentifiers.includes(petTypeIdentifier)) {
          return false;
        }
        petTypeCollectionIdentifiers.push(petTypeIdentifier);
        return true;
      });
      return [...petTypesToAdd, ...petTypeCollection];
    }
    return petTypeCollection;
  }
}
