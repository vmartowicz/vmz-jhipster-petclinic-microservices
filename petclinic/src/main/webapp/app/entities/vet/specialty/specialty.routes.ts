import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import SpecialtyResolve from './route/specialty-routing-resolve.service';

const specialtyRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/specialty').then(m => m.Specialty),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/specialty-detail').then(m => m.SpecialtyDetail),
    resolve: {
      specialty: SpecialtyResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/specialty-update').then(m => m.SpecialtyUpdate),
    resolve: {
      specialty: SpecialtyResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/specialty-update').then(m => m.SpecialtyUpdate),
    resolve: {
      specialty: SpecialtyResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default specialtyRoute;
