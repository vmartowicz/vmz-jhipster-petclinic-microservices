import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { VetService } from 'app/entities/vet/vet/service/vet.service';
import { IVet } from 'app/entities/vet/vet/vet.model';
import { SpecialtyService } from '../service/specialty.service';
import { ISpecialty } from '../specialty.model';

import { SpecialtyFormService } from './specialty-form.service';
import { SpecialtyUpdate } from './specialty-update';

describe('Specialty Management Update Component', () => {
  let comp: SpecialtyUpdate;
  let fixture: ComponentFixture<SpecialtyUpdate>;
  let activatedRoute: ActivatedRoute;
  let specialtyFormService: SpecialtyFormService;
  let specialtyService: SpecialtyService;
  let vetService: VetService;

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

    fixture = TestBed.createComponent(SpecialtyUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    specialtyFormService = TestBed.inject(SpecialtyFormService);
    specialtyService = TestBed.inject(SpecialtyService);
    vetService = TestBed.inject(VetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Vet query and add missing value', () => {
      const specialty: ISpecialty = { id: 20679 };
      const vetses: IVet[] = [{ id: 31928 }];
      specialty.vetses = vetses;

      const vetCollection: IVet[] = [{ id: 31928 }];
      vi.spyOn(vetService, 'query').mockReturnValue(of(new HttpResponse({ body: vetCollection })));
      const additionalVets = [...vetses];
      const expectedCollection: IVet[] = [...additionalVets, ...vetCollection];
      vi.spyOn(vetService, 'addVetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ specialty });
      comp.ngOnInit();

      expect(vetService.query).toHaveBeenCalled();
      expect(vetService.addVetToCollectionIfMissing).toHaveBeenCalledWith(
        vetCollection,
        ...additionalVets.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.vetsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const specialty: ISpecialty = { id: 20679 };
      const vets: IVet = { id: 31928 };
      specialty.vetses = [vets];

      activatedRoute.data = of({ specialty });
      comp.ngOnInit();

      expect(comp.vetsSharedCollection()).toContainEqual(vets);
      expect(comp.specialty).toEqual(specialty);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ISpecialty>();
      const specialty = { id: 29362 };
      vi.spyOn(specialtyFormService, 'getSpecialty').mockReturnValue(specialty);
      vi.spyOn(specialtyService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ specialty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(specialty);
      saveSubject.complete();

      // THEN
      expect(specialtyFormService.getSpecialty).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(specialtyService.update).toHaveBeenCalledWith(expect.objectContaining(specialty));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ISpecialty>();
      const specialty = { id: 29362 };
      vi.spyOn(specialtyFormService, 'getSpecialty').mockReturnValue({ id: null });
      vi.spyOn(specialtyService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ specialty: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(specialty);
      saveSubject.complete();

      // THEN
      expect(specialtyFormService.getSpecialty).toHaveBeenCalled();
      expect(specialtyService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ISpecialty>();
      const specialty = { id: 29362 };
      vi.spyOn(specialtyService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ specialty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(specialtyService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareVet', () => {
      it('should forward to vetService', () => {
        const entity = { id: 31928 };
        const entity2 = { id: 5685 };
        vi.spyOn(vetService, 'compareVet');
        comp.compareVet(entity, entity2);
        expect(vetService.compareVet).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
