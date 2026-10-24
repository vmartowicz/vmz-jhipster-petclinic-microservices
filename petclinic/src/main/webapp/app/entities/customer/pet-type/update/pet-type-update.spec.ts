import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPetType } from '../pet-type.model';
import { PetTypeService } from '../service/pet-type.service';

import { PetTypeFormService } from './pet-type-form.service';
import { PetTypeUpdate } from './pet-type-update';

describe('PetType Management Update Component', () => {
  let comp: PetTypeUpdate;
  let fixture: ComponentFixture<PetTypeUpdate>;
  let activatedRoute: ActivatedRoute;
  let petTypeFormService: PetTypeFormService;
  let petTypeService: PetTypeService;

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

    fixture = TestBed.createComponent(PetTypeUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    petTypeFormService = TestBed.inject(PetTypeFormService);
    petTypeService = TestBed.inject(PetTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const petType: IPetType = { id: 6067 };

      activatedRoute.data = of({ petType });
      comp.ngOnInit();

      expect(comp.petType).toEqual(petType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPetType>();
      const petType = { id: 13878 };
      vi.spyOn(petTypeFormService, 'getPetType').mockReturnValue(petType);
      vi.spyOn(petTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ petType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(petType);
      saveSubject.complete();

      // THEN
      expect(petTypeFormService.getPetType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(petTypeService.update).toHaveBeenCalledWith(expect.objectContaining(petType));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPetType>();
      const petType = { id: 13878 };
      vi.spyOn(petTypeFormService, 'getPetType').mockReturnValue({ id: null });
      vi.spyOn(petTypeService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ petType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(petType);
      saveSubject.complete();

      // THEN
      expect(petTypeFormService.getPetType).toHaveBeenCalled();
      expect(petTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPetType>();
      const petType = { id: 13878 };
      vi.spyOn(petTypeService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ petType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(petTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
