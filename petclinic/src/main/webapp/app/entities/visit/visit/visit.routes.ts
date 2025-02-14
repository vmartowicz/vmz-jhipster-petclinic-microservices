import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VisitResolve from './route/visit-routing-resolve.service';

const visitRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/visit.component').then(m => m.VisitComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/visit-detail.component').then(m => m.VisitDetailComponent),
    resolve: {
      visit: VisitResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/visit-update.component').then(m => m.VisitUpdateComponent),
    resolve: {
      visit: VisitResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/visit-update.component').then(m => m.VisitUpdateComponent),
    resolve: {
      visit: VisitResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default visitRoute;
