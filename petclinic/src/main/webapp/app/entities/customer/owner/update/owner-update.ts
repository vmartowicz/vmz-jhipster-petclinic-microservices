import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOwner } from '../owner.model';
import { OwnerService } from '../service/owner.service';

import { OwnerFormGroup, OwnerFormService } from './owner-form.service';

@Component({
  selector: 'jhi-owner-update',
  templateUrl: './owner-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class OwnerUpdate implements OnInit {
  readonly isSaving = signal(false);
  owner: IOwner | null = null;

  protected ownerService = inject(OwnerService);
  protected ownerFormService = inject(OwnerFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OwnerFormGroup = this.ownerFormService.createOwnerFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ owner }) => {
      this.owner = owner;
      if (owner) {
        this.updateForm(owner);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const owner = this.ownerFormService.getOwner(this.editForm);
    if (owner.id === null) {
      this.subscribeToSaveResponse(this.ownerService.create(owner));
    } else {
      this.subscribeToSaveResponse(this.ownerService.update(owner));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IOwner | null>): void {
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

  protected updateForm(owner: IOwner): void {
    this.owner = owner;
    this.ownerFormService.resetForm(this.editForm, owner);
  }
}
