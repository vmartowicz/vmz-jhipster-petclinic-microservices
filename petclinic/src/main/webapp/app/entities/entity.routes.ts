import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'petclinicApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'pet-type',
    data: { pageTitle: 'petclinicApp.customerPetType.home.title' },
    loadChildren: () => import('./customer/pet-type/pet-type.routes'),
  },
  {
    path: 'owner',
    data: { pageTitle: 'petclinicApp.customerOwner.home.title' },
    loadChildren: () => import('./customer/owner/owner.routes'),
  },
  {
    path: 'pet',
    data: { pageTitle: 'petclinicApp.customerPet.home.title' },
    loadChildren: () => import('./customer/pet/pet.routes'),
  },
  {
    path: 'vet',
    data: { pageTitle: 'petclinicApp.vetVet.home.title' },
    loadChildren: () => import('./vet/vet/vet.routes'),
  },
  {
    path: 'specialty',
    data: { pageTitle: 'petclinicApp.vetSpecialty.home.title' },
    loadChildren: () => import('./vet/specialty/specialty.routes'),
  },
  {
    path: 'visit',
    data: { pageTitle: 'petclinicApp.visitVisit.home.title' },
    loadChildren: () => import('./visit/visit/visit.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
