import NavbarItem from 'app/layouts/navbar/navbar-item.model';

export const EntityNavbarItems: NavbarItem[] = [
  {
    name: 'PetType',
    route: '/pet-type',
    translationKey: 'global.menu.entities.customerPetType',
  },
  {
    name: 'Owner',
    route: '/owner',
    translationKey: 'global.menu.entities.customerOwner',
  },
  {
    name: 'Pet',
    route: '/pet',
    translationKey: 'global.menu.entities.customerPet',
  },
  {
    name: 'Vet',
    route: '/vet',
    translationKey: 'global.menu.entities.vetVet',
  },
  {
    name: 'Specialty',
    route: '/specialty',
    translationKey: 'global.menu.entities.vetSpecialty',
  },
  {
    name: 'Visit',
    route: '/visit',
    translationKey: 'global.menu.entities.visitVisit',
  },
];
