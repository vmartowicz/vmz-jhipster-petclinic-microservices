import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PetTypeResolve from './route/pet-type-routing-resolve.service';

const petTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/pet-type.component').then(m => m.PetTypeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/pet-type-detail.component').then(m => m.PetTypeDetailComponent),
    resolve: {
      petType: PetTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/pet-type-update.component').then(m => m.PetTypeUpdateComponent),
    resolve: {
      petType: PetTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/pet-type-update.component').then(m => m.PetTypeUpdateComponent),
    resolve: {
      petType: PetTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default petTypeRoute;
