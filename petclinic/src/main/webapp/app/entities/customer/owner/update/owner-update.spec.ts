import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IOwner } from '../owner.model';
import { OwnerService } from '../service/owner.service';

import { OwnerFormService } from './owner-form.service';
import { OwnerUpdate } from './owner-update';

describe('Owner Management Update Component', () => {
  let comp: OwnerUpdate;
  let fixture: ComponentFixture<OwnerUpdate>;
  let activatedRoute: ActivatedRoute;
  let ownerFormService: OwnerFormService;
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

    fixture = TestBed.createComponent(OwnerUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ownerFormService = TestBed.inject(OwnerFormService);
    ownerService = TestBed.inject(OwnerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const owner: IOwner = { id: 10278 };

      activatedRoute.data = of({ owner });
      comp.ngOnInit();

      expect(comp.owner).toEqual(owner);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOwner>();
      const owner = { id: 25615 };
      vi.spyOn(ownerFormService, 'getOwner').mockReturnValue(owner);
      vi.spyOn(ownerService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ owner });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(owner);
      saveSubject.complete();

      // THEN
      expect(ownerFormService.getOwner).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ownerService.update).toHaveBeenCalledWith(expect.objectContaining(owner));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOwner>();
      const owner = { id: 25615 };
      vi.spyOn(ownerFormService, 'getOwner').mockReturnValue({ id: null });
      vi.spyOn(ownerService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ owner: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(owner);
      saveSubject.complete();

      // THEN
      expect(ownerFormService.getOwner).toHaveBeenCalled();
      expect(ownerService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IOwner>();
      const owner = { id: 25615 };
      vi.spyOn(ownerService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ owner });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ownerService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
