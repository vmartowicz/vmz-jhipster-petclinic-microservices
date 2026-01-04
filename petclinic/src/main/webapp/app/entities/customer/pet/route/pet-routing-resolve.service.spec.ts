import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, convertToParamMap } from '@angular/router';

import { of } from 'rxjs';

import { IPet } from '../pet.model';
import { PetService } from '../service/pet.service';

import petResolve from './pet-routing-resolve.service';

describe('Pet routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: PetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate');
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(PetService);
  });

  describe('resolve', () => {
    it('should return IPet returned by find', async () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      await new Promise<void>(resolve => {
        TestBed.runInInjectionContext(() => {
          petResolve(mockActivatedRouteSnapshot).subscribe({
            next(result) {
              // THEN
              expect(service.find).toHaveBeenCalledWith(123);
              expect(result).toEqual({ id: 123 });
              resolve();
            },
          });
        });
      });
    });

    it('should return null if id is not provided', async () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      await new Promise<void>(resolve => {
        TestBed.runInInjectionContext(() => {
          petResolve(mockActivatedRouteSnapshot).subscribe({
            next(result) {
              // THEN
              expect(service.find).not.toHaveBeenCalled();
              expect(result).toEqual(null);
              resolve();
            },
          });
        });
      });
    });

    it('should route to 404 page if data not found in server', async () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IPet>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      await new Promise<void>(resolve => {
        TestBed.runInInjectionContext(() => {
          petResolve(mockActivatedRouteSnapshot).subscribe({
            complete() {
              // THEN
              expect(service.find).toHaveBeenCalledWith(123);
              expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
              resolve();
            },
          });
        });
      });
    });
  });
});
