import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import PetResolve from './route/pet-routing-resolve.service';

const petRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/pet').then(m => m.Pet),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/pet-detail').then(m => m.PetDetail),
    resolve: {
      pet: PetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/pet-update').then(m => m.PetUpdate),
    resolve: {
      pet: PetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/pet-update').then(m => m.PetUpdate),
    resolve: {
      pet: PetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default petRoute;
