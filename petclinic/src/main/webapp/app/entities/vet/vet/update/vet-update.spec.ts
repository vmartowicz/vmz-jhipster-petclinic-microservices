import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { TranslateModule } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { SpecialtyService } from 'app/entities/vet/specialty/service/specialty.service';
import { ISpecialty } from 'app/entities/vet/specialty/specialty.model';
import { VetService } from '../service/vet.service';
import { IVet } from '../vet.model';

import { VetFormService } from './vet-form.service';
import { VetUpdate } from './vet-update';

describe('Vet Management Update Component', () => {
  let comp: VetUpdate;
  let fixture: ComponentFixture<VetUpdate>;
  let activatedRoute: ActivatedRoute;
  let vetFormService: VetFormService;
  let vetService: VetService;
  let specialtyService: SpecialtyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(VetUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vetFormService = TestBed.inject(VetFormService);
    vetService = TestBed.inject(VetService);
    specialtyService = TestBed.inject(SpecialtyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Specialty query and add missing value', () => {
      const vet: IVet = { id: 5685 };
      const specialties: ISpecialty[] = [{ id: 29362 }];
      vet.specialties = specialties;

      const specialtyCollection: ISpecialty[] = [{ id: 29362 }];
      jest.spyOn(specialtyService, 'query').mockReturnValue(of(new HttpResponse({ body: specialtyCollection })));
      const additionalSpecialties = [...specialties];
      const expectedCollection: ISpecialty[] = [...additionalSpecialties, ...specialtyCollection];
      jest.spyOn(specialtyService, 'addSpecialtyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vet });
      comp.ngOnInit();

      expect(specialtyService.query).toHaveBeenCalled();
      expect(specialtyService.addSpecialtyToCollectionIfMissing).toHaveBeenCalledWith(
        specialtyCollection,
        ...additionalSpecialties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.specialtiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const vet: IVet = { id: 5685 };
      const specialties: ISpecialty = { id: 29362 };
      vet.specialties = [specialties];

      activatedRoute.data = of({ vet });
      comp.ngOnInit();

      expect(comp.specialtiesSharedCollection()).toContainEqual(specialties);
      expect(comp.vet).toEqual(vet);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVet>>();
      const vet = { id: 31928 };
      jest.spyOn(vetFormService, 'getVet').mockReturnValue(vet);
      jest.spyOn(vetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vet }));
      saveSubject.complete();

      // THEN
      expect(vetFormService.getVet).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vetService.update).toHaveBeenCalledWith(expect.objectContaining(vet));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVet>>();
      const vet = { id: 31928 };
      jest.spyOn(vetFormService, 'getVet').mockReturnValue({ id: null });
      jest.spyOn(vetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vet: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vet }));
      saveSubject.complete();

      // THEN
      expect(vetFormService.getVet).toHaveBeenCalled();
      expect(vetService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVet>>();
      const vet = { id: 31928 };
      jest.spyOn(vetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vetService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSpecialty', () => {
      it('should forward to specialtyService', () => {
        const entity = { id: 29362 };
        const entity2 = { id: 20679 };
        jest.spyOn(specialtyService, 'compareSpecialty');
        comp.compareSpecialty(entity, entity2);
        expect(specialtyService.compareSpecialty).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
