import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VetResolve from './route/vet-routing-resolve.service';

const vetRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/vet.component').then(m => m.VetComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/vet-detail.component').then(m => m.VetDetailComponent),
    resolve: {
      vet: VetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/vet-update.component').then(m => m.VetUpdateComponent),
    resolve: {
      vet: VetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/vet-update.component').then(m => m.VetUpdateComponent),
    resolve: {
      vet: VetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vetRoute;
