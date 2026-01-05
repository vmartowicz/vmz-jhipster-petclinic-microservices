import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import OwnerResolve from './route/owner-routing-resolve.service';

const ownerRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/owner').then(m => m.Owner),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/owner-detail').then(m => m.OwnerDetail),
    resolve: {
      owner: OwnerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/owner-update').then(m => m.OwnerUpdate),
    resolve: {
      owner: OwnerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/owner-update').then(m => m.OwnerUpdate),
    resolve: {
      owner: OwnerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ownerRoute;
