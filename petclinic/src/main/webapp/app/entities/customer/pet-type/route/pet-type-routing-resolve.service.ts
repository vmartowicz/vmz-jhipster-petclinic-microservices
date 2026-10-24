import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPetType } from '../pet-type.model';
import { PetTypeService } from '../service/pet-type.service';

const petTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IPetType> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PetTypeService);
    return service.find(id).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          router.navigate(['404']);
        } else {
          router.navigate(['error']);
        }
        return EMPTY;
      }),
    );
  }

  return of(null);
};

export default petTypeResolve;
