import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPet } from '../pet.model';
import { PetService } from '../service/pet.service';

const petResolve = (route: ActivatedRouteSnapshot): Observable<null | IPet> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PetService);
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

export default petResolve;
