import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IOwner } from 'app/entities/customer/owner/owner.model';
import { OwnerService } from 'app/entities/customer/owner/service/owner.service';
import { IPetType } from 'app/entities/customer/pet-type/pet-type.model';
import { PetTypeService } from 'app/entities/customer/pet-type/service/pet-type.service';
import { IPet } from '../pet.model';
import { PetService } from '../service/pet.service';

import { PetFormService } from './pet-form.service';
import { PetUpdate } from './pet-update';

describe('Pet Management Update Component', () => {
  let comp: PetUpdate;
  let fixture: ComponentFixture<PetUpdate>;
  let activatedRoute: ActivatedRoute;
  let petFormService: PetFormService;
  let petService: PetService;
  let petTypeService: PetTypeService;
  let ownerService: OwnerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(PetUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    petFormService = TestBed.inject(PetFormService);
    petService = TestBed.inject(PetService);
    petTypeService = TestBed.inject(PetTypeService);
    ownerService = TestBed.inject(OwnerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call PetType query and add missing value', () => {
      const pet: IPet = { id: 28893 };
      const type: IPetType = { id: 13878 };
      pet.type = type;

      const petTypeCollection: IPetType[] = [{ id: 13878 }];
      vi.spyOn(petTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: petTypeCollection })));
      const additionalPetTypes = [type];
      const expectedCollection: IPetType[] = [...additionalPetTypes, ...petTypeCollection];
      vi.spyOn(petTypeService, 'addPetTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pet });
      comp.ngOnInit();

      expect(petTypeService.query).toHaveBeenCalled();
      expect(petTypeService.addPetTypeToCollectionIfMissing).toHaveBeenCalledWith(
        petTypeCollection,
        ...additionalPetTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.petTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Owner query and add missing value', () => {
      const pet: IPet = { id: 28893 };
      const owner: IOwner = { id: 25615 };
      pet.owner = owner;

      const ownerCollection: IOwner[] = [{ id: 25615 }];
      vi.spyOn(ownerService, 'query').mockReturnValue(of(new HttpResponse({ body: ownerCollection })));
      const additionalOwners = [owner];
      const expectedCollection: IOwner[] = [...additionalOwners, ...ownerCollection];
      vi.spyOn(ownerService, 'addOwnerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pet });
      comp.ngOnInit();

      expect(ownerService.query).toHaveBeenCalled();
      expect(ownerService.addOwnerToCollectionIfMissing).toHaveBeenCalledWith(
        ownerCollection,
        ...additionalOwners.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ownersSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const pet: IPet = { id: 28893 };
      const type: IPetType = { id: 13878 };
      pet.type = type;
      const owner: IOwner = { id: 25615 };
      pet.owner = owner;

      activatedRoute.data = of({ pet });
      comp.ngOnInit();

      expect(comp.petTypesSharedCollection()).toContainEqual(type);
      expect(comp.ownersSharedCollection()).toContainEqual(owner);
      expect(comp.pet).toEqual(pet);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPet>();
      const pet = { id: 23154 };
      vi.spyOn(petFormService, 'getPet').mockReturnValue(pet);
      vi.spyOn(petService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(pet);
      saveSubject.complete();

      // THEN
      expect(petFormService.getPet).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(petService.update).toHaveBeenCalledWith(expect.objectContaining(pet));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPet>();
      const pet = { id: 23154 };
      vi.spyOn(petFormService, 'getPet').mockReturnValue({ id: null });
      vi.spyOn(petService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pet: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(pet);
      saveSubject.complete();

      // THEN
      expect(petFormService.getPet).toHaveBeenCalled();
      expect(petService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPet>();
      const pet = { id: 23154 };
      vi.spyOn(petService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(petService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePetType', () => {
      it('should forward to petTypeService', () => {
        const entity = { id: 13878 };
        const entity2 = { id: 6067 };
        vi.spyOn(petTypeService, 'comparePetType');
        comp.comparePetType(entity, entity2);
        expect(petTypeService.comparePetType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOwner', () => {
      it('should forward to ownerService', () => {
        const entity = { id: 25615 };
        const entity2 = { id: 10278 };
        vi.spyOn(ownerService, 'compareOwner');
        comp.compareOwner(entity, entity2);
        expect(ownerService.compareOwner).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
