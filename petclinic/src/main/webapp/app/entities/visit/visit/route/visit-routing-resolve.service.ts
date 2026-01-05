import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { VisitService } from '../service/visit.service';
import { IVisit } from '../visit.model';

const visitResolve = (route: ActivatedRouteSnapshot): Observable<null | IVisit> => {
  const id = route.params.id;
  if (id) {
    return inject(VisitService)
      .find(id)
      .pipe(
        mergeMap((visit: HttpResponse<IVisit>) => {
          if (visit.body) {
            return of(visit.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default visitResolve;
