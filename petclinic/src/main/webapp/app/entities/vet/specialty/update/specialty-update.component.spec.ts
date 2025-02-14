import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IVet } from 'app/entities/vet/vet/vet.model';
import { VetService } from 'app/entities/vet/vet/service/vet.service';
import { SpecialtyService } from '../service/specialty.service';
import { ISpecialty } from '../specialty.model';
import { SpecialtyFormService } from './specialty-form.service';

import { SpecialtyUpdateComponent } from './specialty-update.component';

describe('Specialty Management Update Component', () => {
  let comp: SpecialtyUpdateComponent;
  let fixture: ComponentFixture<SpecialtyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let specialtyFormService: SpecialtyFormService;
  let specialtyService: SpecialtyService;
  let vetService: VetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SpecialtyUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SpecialtyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SpecialtyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    specialtyFormService = TestBed.inject(SpecialtyFormService);
    specialtyService = TestBed.inject(SpecialtyService);
    vetService = TestBed.inject(VetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Vet query and add missing value', () => {
      const specialty: ISpecialty = { id: 20679 };
      const vets: IVet[] = [{ id: 31928 }];
      specialty.vets = vets;

      const vetCollection: IVet[] = [{ id: 31928 }];
      jest.spyOn(vetService, 'query').mockReturnValue(of(new HttpResponse({ body: vetCollection })));
      const additionalVets = [...vets];
      const expectedCollection: IVet[] = [...additionalVets, ...vetCollection];
      jest.spyOn(vetService, 'addVetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ specialty });
      comp.ngOnInit();

      expect(vetService.query).toHaveBeenCalled();
      expect(vetService.addVetToCollectionIfMissing).toHaveBeenCalledWith(vetCollection, ...additionalVets.map(expect.objectContaining));
      expect(comp.vetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const specialty: ISpecialty = { id: 20679 };
      const vets: IVet = { id: 31928 };
      specialty.vets = [vets];

      activatedRoute.data = of({ specialty });
      comp.ngOnInit();

      expect(comp.vetsSharedCollection).toContainEqual(vets);
      expect(comp.specialty).toEqual(specialty);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpecialty>>();
      const specialty = { id: 29362 };
      jest.spyOn(specialtyFormService, 'getSpecialty').mockReturnValue(specialty);
      jest.spyOn(specialtyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ specialty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: specialty }));
      saveSubject.complete();

      // THEN
      expect(specialtyFormService.getSpecialty).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(specialtyService.update).toHaveBeenCalledWith(expect.objectContaining(specialty));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpecialty>>();
      const specialty = { id: 29362 };
      jest.spyOn(specialtyFormService, 'getSpecialty').mockReturnValue({ id: null });
      jest.spyOn(specialtyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ specialty: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: specialty }));
      saveSubject.complete();

      // THEN
      expect(specialtyFormService.getSpecialty).toHaveBeenCalled();
      expect(specialtyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpecialty>>();
      const specialty = { id: 29362 };
      jest.spyOn(specialtyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ specialty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(specialtyService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareVet', () => {
      it('Should forward to vetService', () => {
        const entity = { id: 31928 };
        const entity2 = { id: 5685 };
        jest.spyOn(vetService, 'compareVet');
        comp.compareVet(entity, entity2);
        expect(vetService.compareVet).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
