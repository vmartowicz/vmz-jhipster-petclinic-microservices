import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PetResolve from './route/pet-routing-resolve.service';

const petRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/pet').then(m => m.Pet),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/pet-detail').then(m => m.PetDetail),
    resolve: {
      pet: PetResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/pet-update').then(m => m.PetUpdate),
    resolve: {
      pet: PetResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/pet-update').then(m => m.PetUpdate),
    resolve: {
      pet: PetResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default petRoute;
