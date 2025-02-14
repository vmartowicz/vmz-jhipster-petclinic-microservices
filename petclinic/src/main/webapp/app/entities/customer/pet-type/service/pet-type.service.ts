import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPetType, NewPetType } from '../pet-type.model';

export type PartialUpdatePetType = Partial<IPetType> & Pick<IPetType, 'id'>;

export type EntityResponseType = HttpResponse<IPetType>;
export type EntityArrayResponseType = HttpResponse<IPetType[]>;

@Injectable({ providedIn: 'root' })
export class PetTypeService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pet-types', 'customer');

  create(petType: NewPetType): Observable<EntityResponseType> {
    return this.http.post<IPetType>(this.resourceUrl, petType, { observe: 'response' });
  }

  update(petType: IPetType): Observable<EntityResponseType> {
    return this.http.put<IPetType>(`${this.resourceUrl}/${this.getPetTypeIdentifier(petType)}`, petType, { observe: 'response' });
  }

  partialUpdate(petType: PartialUpdatePetType): Observable<EntityResponseType> {
    return this.http.patch<IPetType>(`${this.resourceUrl}/${this.getPetTypeIdentifier(petType)}`, petType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPetType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPetType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
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
    const petTypes: Type[] = petTypesToCheck.filter(isPresent);
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
