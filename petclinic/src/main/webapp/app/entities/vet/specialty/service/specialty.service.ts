import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { microserviceContextPath, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ISpecialty, NewSpecialty } from '../specialty.model';

export type PartialUpdateSpecialty = Partial<ISpecialty> & Pick<ISpecialty, 'id'>;

@Service()
export class SpecialtiesService {
  readonly specialtiesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly specialtiesResource = httpResource<ISpecialty[]>(() => {
    const params = this.specialtiesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of specialty that have been fetched. It is updated when the specialtiesResource emits a new value.
   * In case of error while fetching the specialties, the signal is set to an empty array.
   */
  readonly specialties = computed(() => (this.specialtiesResource.hasValue() ? this.specialtiesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}${microserviceContextPath}vet/api/specialties`;
}

@Service()
export class SpecialtyService extends SpecialtiesService {
  protected readonly http = inject(HttpClient);

  create(specialty: NewSpecialty): Observable<ISpecialty> {
    return this.http.post<ISpecialty>(this.resourceUrl, specialty);
  }

  update(specialty: ISpecialty): Observable<ISpecialty> {
    return this.http.put<ISpecialty>(`${this.resourceUrl}/${encodeURIComponent(this.getSpecialtyIdentifier(specialty))}`, specialty);
  }

  partialUpdate(specialty: PartialUpdateSpecialty): Observable<ISpecialty> {
    return this.http.patch<ISpecialty>(`${this.resourceUrl}/${encodeURIComponent(this.getSpecialtyIdentifier(specialty))}`, specialty);
  }

  find(id: number): Observable<ISpecialty> {
    return this.http.get<ISpecialty>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ISpecialty[]>> {
    const options = createRequestOption(req);
    return this.http.get<ISpecialty[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getSpecialtyIdentifier(specialty: Pick<ISpecialty, 'id'>): number {
    return specialty.id;
  }

  compareSpecialty(o1: Pick<ISpecialty, 'id'> | null, o2: Pick<ISpecialty, 'id'> | null): boolean {
    return o1 && o2 ? this.getSpecialtyIdentifier(o1) === this.getSpecialtyIdentifier(o2) : o1 === o2;
  }

  addSpecialtyToCollectionIfMissing<Type extends Pick<ISpecialty, 'id'>>(
    specialtyCollection: Type[],
    ...specialtiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const specialties: Type[] = specialtiesToCheck.filter(specialtyItem => specialtyItem !== null && specialtyItem !== undefined);
    if (specialties.length > 0) {
      const specialtyCollectionIdentifiers = specialtyCollection.map(specialtyItem => this.getSpecialtyIdentifier(specialtyItem));
      const specialtiesToAdd = specialties.filter(specialtyItem => {
        const specialtyIdentifier = this.getSpecialtyIdentifier(specialtyItem);
        if (specialtyCollectionIdentifiers.includes(specialtyIdentifier)) {
          return false;
        }
        specialtyCollectionIdentifiers.push(specialtyIdentifier);
        return true;
      });
      return [...specialtiesToAdd, ...specialtyCollection];
    }
    return specialtyCollection;
  }
}
