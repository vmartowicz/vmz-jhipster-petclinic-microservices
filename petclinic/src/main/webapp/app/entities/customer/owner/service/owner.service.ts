import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { microserviceContextPath, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IOwner, NewOwner } from '../owner.model';

export type PartialUpdateOwner = Partial<IOwner> & Pick<IOwner, 'id'>;

@Service()
export class OwnersService {
  readonly ownersParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(undefined);
  readonly ownersResource = httpResource<IOwner[]>(() => {
    const params = this.ownersParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of owner that have been fetched. It is updated when the ownersResource emits a new value.
   * In case of error while fetching the owners, the signal is set to an empty array.
   */
  readonly owners = computed(() => (this.ownersResource.hasValue() ? this.ownersResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}${microserviceContextPath}customer/api/owners`;
}

@Service()
export class OwnerService extends OwnersService {
  protected readonly http = inject(HttpClient);

  create(owner: NewOwner): Observable<IOwner> {
    return this.http.post<IOwner>(this.resourceUrl, owner);
  }

  update(owner: IOwner): Observable<IOwner> {
    return this.http.put<IOwner>(`${this.resourceUrl}/${encodeURIComponent(this.getOwnerIdentifier(owner))}`, owner);
  }

  partialUpdate(owner: PartialUpdateOwner): Observable<IOwner> {
    return this.http.patch<IOwner>(`${this.resourceUrl}/${encodeURIComponent(this.getOwnerIdentifier(owner))}`, owner);
  }

  find(id: number): Observable<IOwner> {
    return this.http.get<IOwner>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IOwner[]>> {
    const options = createRequestOption(req);
    return this.http.get<IOwner[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getOwnerIdentifier(owner: Pick<IOwner, 'id'>): number {
    return owner.id;
  }

  compareOwner(o1: Pick<IOwner, 'id'> | null, o2: Pick<IOwner, 'id'> | null): boolean {
    return o1 && o2 ? this.getOwnerIdentifier(o1) === this.getOwnerIdentifier(o2) : o1 === o2;
  }

  addOwnerToCollectionIfMissing<Type extends Pick<IOwner, 'id'>>(
    ownerCollection: Type[],
    ...ownersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const owners: Type[] = ownersToCheck.filter(ownerItem => ownerItem !== null && ownerItem !== undefined);
    if (owners.length > 0) {
      const ownerCollectionIdentifiers = ownerCollection.map(ownerItem => this.getOwnerIdentifier(ownerItem));
      const ownersToAdd = owners.filter(ownerItem => {
        const ownerIdentifier = this.getOwnerIdentifier(ownerItem);
        if (ownerCollectionIdentifiers.includes(ownerIdentifier)) {
          return false;
        }
        ownerCollectionIdentifiers.push(ownerIdentifier);
        return true;
      });
      return [...ownersToAdd, ...ownerCollection];
    }
    return ownerCollection;
  }
}
