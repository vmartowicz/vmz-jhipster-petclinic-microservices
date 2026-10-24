import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'user-management',
    title: 'userManagement.home.title',
    loadChildren: () => import('./admin/user-management/user-management.routes'),
  },
  {
    path: 'authority',
    title: 'petclinicApp.adminAuthority.home.title',
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'pet-type',
    title: 'petclinicApp.customerPetType.home.title',
    loadChildren: () => import('./customer/pet-type/pet-type.routes'),
  },
  {
    path: 'owner',
    title: 'petclinicApp.customerOwner.home.title',
    loadChildren: () => import('./customer/owner/owner.routes'),
  },
  {
    path: 'pet',
    title: 'petclinicApp.customerPet.home.title',
    loadChildren: () => import('./customer/pet/pet.routes'),
  },
  {
    path: 'vet',
    title: 'petclinicApp.vetVet.home.title',
    loadChildren: () => import('./vet/vet/vet.routes'),
  },
  {
    path: 'specialty',
    title: 'petclinicApp.vetSpecialty.home.title',
    loadChildren: () => import('./vet/specialty/specialty.routes'),
  },
  {
    path: 'visit',
    title: 'petclinicApp.visitVisit.home.title',
    loadChildren: () => import('./visit/visit/visit.routes'),
  },
  // jhipster-needle-add-entity-route - JHipster will add entity modules routes here
];

export default routes;
