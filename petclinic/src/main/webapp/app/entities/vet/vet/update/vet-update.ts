import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { SpecialtyService } from 'app/entities/vet/specialty/service/specialty.service';
import { ISpecialty } from 'app/entities/vet/specialty/specialty.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { VetService } from '../service/vet.service';
import { IVet } from '../vet.model';

import { VetFormGroup, VetFormService } from './vet-form.service';

@Component({
  selector: 'jhi-vet-update',
  templateUrl: './vet-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class VetUpdate implements OnInit {
  readonly isSaving = signal(false);
  vet: IVet | null = null;

  specialtiesSharedCollection = signal<ISpecialty[]>([]);

  protected vetService = inject(VetService);
  protected vetFormService = inject(VetFormService);
  protected specialtyService = inject(SpecialtyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VetFormGroup = this.vetFormService.createVetFormGroup();

  compareSpecialty = (o1: ISpecialty | null, o2: ISpecialty | null): boolean => this.specialtyService.compareSpecialty(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vet }) => {
      this.vet = vet;
      if (vet) {
        this.updateForm(vet);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const vet = this.vetFormService.getVet(this.editForm);
    if (vet.id === null) {
      this.subscribeToSaveResponse(this.vetService.create(vet));
    } else {
      this.subscribeToSaveResponse(this.vetService.update(vet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IVet | null>): void {
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

  protected updateForm(vet: IVet): void {
    this.vet = vet;
    this.vetFormService.resetForm(this.editForm, vet);

    this.specialtiesSharedCollection.update(specialties =>
      this.specialtyService.addSpecialtyToCollectionIfMissing<ISpecialty>(specialties, ...(vet.specialtieses ?? [])),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.specialtyService
      .query()
      .pipe(map((res: HttpResponse<ISpecialty[]>) => res.body ?? []))
      .pipe(
        map((specialties: ISpecialty[]) =>
          this.specialtyService.addSpecialtyToCollectionIfMissing<ISpecialty>(specialties, ...(this.vet?.specialtieses ?? [])),
        ),
      )
      .subscribe((specialties: ISpecialty[]) => this.specialtiesSharedCollection.set(specialties));
  }
}
