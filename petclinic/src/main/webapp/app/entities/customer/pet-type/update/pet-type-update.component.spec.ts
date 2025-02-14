import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { PetTypeService } from '../service/pet-type.service';
import { IPetType } from '../pet-type.model';
import { PetTypeFormService } from './pet-type-form.service';

import { PetTypeUpdateComponent } from './pet-type-update.component';

describe('PetType Management Update Component', () => {
  let comp: PetTypeUpdateComponent;
  let fixture: ComponentFixture<PetTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let petTypeFormService: PetTypeFormService;
  let petTypeService: PetTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PetTypeUpdateComponent],
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
      .overrideTemplate(PetTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PetTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    petTypeFormService = TestBed.inject(PetTypeFormService);
    petTypeService = TestBed.inject(PetTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const petType: IPetType = { id: 6067 };

      activatedRoute.data = of({ petType });
      comp.ngOnInit();

      expect(comp.petType).toEqual(petType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPetType>>();
      const petType = { id: 13878 };
      jest.spyOn(petTypeFormService, 'getPetType').mockReturnValue(petType);
      jest.spyOn(petTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ petType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: petType }));
      saveSubject.complete();

      // THEN
      expect(petTypeFormService.getPetType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(petTypeService.update).toHaveBeenCalledWith(expect.objectContaining(petType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPetType>>();
      const petType = { id: 13878 };
      jest.spyOn(petTypeFormService, 'getPetType').mockReturnValue({ id: null });
      jest.spyOn(petTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ petType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: petType }));
      saveSubject.complete();

      // THEN
      expect(petTypeFormService.getPetType).toHaveBeenCalled();
      expect(petTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPetType>>();
      const petType = { id: 13878 };
      jest.spyOn(petTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ petType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(petTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
