import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import VetResolve from './route/vet-routing-resolve.service';

const vetRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/vet').then(m => m.Vet),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/vet-detail').then(m => m.VetDetail),
    resolve: {
      vet: VetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/vet-update').then(m => m.VetUpdate),
    resolve: {
      vet: VetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/vet-update').then(m => m.VetUpdate),
    resolve: {
      vet: VetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vetRoute;
