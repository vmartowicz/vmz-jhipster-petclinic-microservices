import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import OwnerResolve from './route/owner-routing-resolve.service';

const ownerRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/owner').then(m => m.Owner),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/owner-detail').then(m => m.OwnerDetail),
    resolve: {
      owner: OwnerResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/owner-update').then(m => m.OwnerUpdate),
    resolve: {
      owner: OwnerResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/owner-update').then(m => m.OwnerUpdate),
    resolve: {
      owner: OwnerResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default ownerRoute;
