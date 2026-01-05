import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import PetTypeResolve from './route/pet-type-routing-resolve.service';

const petTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/pet-type').then(m => m.PetType),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/pet-type-detail').then(m => m.PetTypeDetail),
    resolve: {
      petType: PetTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/pet-type-update').then(m => m.PetTypeUpdate),
    resolve: {
      petType: PetTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/pet-type-update').then(m => m.PetTypeUpdate),
    resolve: {
      petType: PetTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default petTypeRoute;
