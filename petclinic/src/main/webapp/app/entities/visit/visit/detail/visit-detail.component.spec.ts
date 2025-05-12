import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { VisitDetailComponent } from './visit-detail.component';

describe('Visit Management Detail Component', () => {
  let comp: VisitDetailComponent;
  let fixture: ComponentFixture<VisitDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VisitDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./visit-detail.component').then(m => m.VisitDetailComponent),
              resolve: { visit: () => of({ id: 31581 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VisitDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VisitDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load visit on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VisitDetailComponent);

      // THEN
      expect(instance.visit()).toEqual(expect.objectContaining({ id: 31581 }));
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
