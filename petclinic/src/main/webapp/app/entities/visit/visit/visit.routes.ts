import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import VisitResolve from './route/visit-routing-resolve.service';

const visitRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/visit').then(m => m.Visit),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/visit-detail').then(m => m.VisitDetail),
    resolve: {
      visit: VisitResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/visit-update').then(m => m.VisitUpdate),
    resolve: {
      visit: VisitResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/visit-update').then(m => m.VisitUpdate),
    resolve: {
      visit: VisitResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default visitRoute;
