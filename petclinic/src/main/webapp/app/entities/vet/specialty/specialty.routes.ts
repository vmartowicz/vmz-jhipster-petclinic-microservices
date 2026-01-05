import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import SpecialtyResolve from './route/specialty-routing-resolve.service';

const specialtyRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/specialty').then(m => m.Specialty),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/specialty-detail').then(m => m.SpecialtyDetail),
    resolve: {
      specialty: SpecialtyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/specialty-update').then(m => m.SpecialtyUpdate),
    resolve: {
      specialty: SpecialtyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/specialty-update').then(m => m.SpecialtyUpdate),
    resolve: {
      specialty: SpecialtyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default specialtyRoute;
