import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { VetDetailComponent } from './vet-detail.component';

describe('Vet Management Detail Component', () => {
  let comp: VetDetailComponent;
  let fixture: ComponentFixture<VetDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VetDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./vet-detail.component').then(m => m.VetDetailComponent),
              resolve: { vet: () => of({ id: 31928 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VetDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VetDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load vet on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VetDetailComponent);

      // THEN
      expect(instance.vet()).toEqual(expect.objectContaining({ id: 31928 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
