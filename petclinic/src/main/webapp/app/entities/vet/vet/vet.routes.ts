import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import VetResolve from './route/vet-routing-resolve.service';

const vetRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/vet').then(m => m.Vet),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/vet-detail').then(m => m.VetDetail),
    resolve: {
      vet: VetResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/vet-update').then(m => m.VetUpdate),
    resolve: {
      vet: VetResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/vet-update').then(m => m.VetUpdate),
    resolve: {
      vet: VetResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default vetRoute;
