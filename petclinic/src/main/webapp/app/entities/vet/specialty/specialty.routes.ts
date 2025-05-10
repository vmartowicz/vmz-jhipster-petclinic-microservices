import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SpecialtyResolve from './route/specialty-routing-resolve.service';

const specialtyRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/specialty.component').then(m => m.SpecialtyComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/specialty-detail.component').then(m => m.SpecialtyDetailComponent),
    resolve: {
      specialty: SpecialtyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/specialty-update.component').then(m => m.SpecialtyUpdateComponent),
    resolve: {
      specialty: SpecialtyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/specialty-update.component').then(m => m.SpecialtyUpdateComponent),
    resolve: {
      specialty: SpecialtyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default specialtyRoute;
