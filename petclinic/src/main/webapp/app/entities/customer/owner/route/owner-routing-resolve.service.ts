import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IOwner } from '../owner.model';
import { OwnerService } from '../service/owner.service';

const ownerResolve = (route: ActivatedRouteSnapshot): Observable<null | IOwner> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(OwnerService);
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

export default ownerResolve;
