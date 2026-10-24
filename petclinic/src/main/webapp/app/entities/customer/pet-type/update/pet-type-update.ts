import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPetType } from '../pet-type.model';
import { PetTypeService } from '../service/pet-type.service';

import { PetTypeFormGroup, PetTypeFormService } from './pet-type-form.service';

@Component({
  selector: 'jhi-pet-type-update',
  templateUrl: './pet-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PetTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  petType: IPetType | null = null;

  protected petTypeService = inject(PetTypeService);
  protected petTypeFormService = inject(PetTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PetTypeFormGroup = this.petTypeFormService.createPetTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ petType }) => {
      this.petType = petType;
      if (petType) {
        this.updateForm(petType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const petType = this.petTypeFormService.getPetType(this.editForm);
    if (petType.id === null) {
      this.subscribeToSaveResponse(this.petTypeService.create(petType));
    } else {
      this.subscribeToSaveResponse(this.petTypeService.update(petType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPetType | null>): void {
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

  protected updateForm(petType: IPetType): void {
    this.petType = petType;
    this.petTypeFormService.resetForm(this.editForm, petType);
  }
}
