import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PetTypeResolve from './route/pet-type-routing-resolve.service';

const petTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/pet-type').then(m => m.PetType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/pet-type-detail').then(m => m.PetTypeDetail),
    resolve: {
      petType: PetTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/pet-type-update').then(m => m.PetTypeUpdate),
    resolve: {
      petType: PetTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/pet-type-update').then(m => m.PetTypeUpdate),
    resolve: {
      petType: PetTypeResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default petTypeRoute;
