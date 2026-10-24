import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { VisitService } from '../service/visit.service';
import { IVisit } from '../visit.model';

import { VisitFormGroup, VisitFormService } from './visit-form.service';

@Component({
  selector: 'jhi-visit-update',
  templateUrl: './visit-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class VisitUpdate implements OnInit {
  readonly isSaving = signal(false);
  visit: IVisit | null = null;

  protected visitService = inject(VisitService);
  protected visitFormService = inject(VisitFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VisitFormGroup = this.visitFormService.createVisitFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visit }) => {
      this.visit = visit;
      if (visit) {
        this.updateForm(visit);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const visit = this.visitFormService.getVisit(this.editForm);
    if (visit.id === null) {
      this.subscribeToSaveResponse(this.visitService.create(visit));
    } else {
      this.subscribeToSaveResponse(this.visitService.update(visit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IVisit | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(visit: IVisit): void {
    this.visit = visit;
    this.visitFormService.resetForm(this.editForm, visit);
  }
}
