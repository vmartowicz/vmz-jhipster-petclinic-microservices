import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { microserviceContextPath, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IVet, NewVet } from '../vet.model';

export type PartialUpdateVet = Partial<IVet> & Pick<IVet, 'id'>;

@Service()
export class VetsService {
  readonly vetsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(undefined);
  readonly vetsResource = httpResource<IVet[]>(() => {
    const params = this.vetsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of vet that have been fetched. It is updated when the vetsResource emits a new value.
   * In case of error while fetching the vets, the signal is set to an empty array.
   */
  readonly vets = computed(() => (this.vetsResource.hasValue() ? this.vetsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}${microserviceContextPath}vet/api/vets`;
}

@Service()
export class VetService extends VetsService {
  protected readonly http = inject(HttpClient);

  create(vet: NewVet): Observable<IVet> {
    return this.http.post<IVet>(this.resourceUrl, vet);
  }

  update(vet: IVet): Observable<IVet> {
    return this.http.put<IVet>(`${this.resourceUrl}/${encodeURIComponent(this.getVetIdentifier(vet))}`, vet);
  }

  partialUpdate(vet: PartialUpdateVet): Observable<IVet> {
    return this.http.patch<IVet>(`${this.resourceUrl}/${encodeURIComponent(this.getVetIdentifier(vet))}`, vet);
  }

  find(id: number): Observable<IVet> {
    return this.http.get<IVet>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IVet[]>> {
    const options = createRequestOption(req);
    return this.http.get<IVet[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getVetIdentifier(vet: Pick<IVet, 'id'>): number {
    return vet.id;
  }

  compareVet(o1: Pick<IVet, 'id'> | null, o2: Pick<IVet, 'id'> | null): boolean {
    return o1 && o2 ? this.getVetIdentifier(o1) === this.getVetIdentifier(o2) : o1 === o2;
  }

  addVetToCollectionIfMissing<Type extends Pick<IVet, 'id'>>(vetCollection: Type[], ...vetsToCheck: (Type | null | undefined)[]): Type[] {
    const vets: Type[] = vetsToCheck.filter(vetItem => vetItem !== null && vetItem !== undefined);
    if (vets.length > 0) {
      const vetCollectionIdentifiers = vetCollection.map(vetItem => this.getVetIdentifier(vetItem));
      const vetsToAdd = vets.filter(vetItem => {
        const vetIdentifier = this.getVetIdentifier(vetItem);
        if (vetCollectionIdentifiers.includes(vetIdentifier)) {
          return false;
        }
        vetCollectionIdentifiers.push(vetIdentifier);
        return true;
      });
      return [...vetsToAdd, ...vetCollection];
    }
    return vetCollection;
  }
}
