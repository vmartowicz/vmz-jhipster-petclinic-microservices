import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { SpecialtyService } from '../service/specialty.service';
import { ISpecialty } from '../specialty.model';

const specialtyResolve = (route: ActivatedRouteSnapshot): Observable<null | ISpecialty> => {
  const id = route.params.id;
  if (id) {
    return inject(SpecialtyService)
      .find(id)
      .pipe(
        mergeMap((specialty: HttpResponse<ISpecialty>) => {
          if (specialty.body) {
            return of(specialty.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default specialtyResolve;
