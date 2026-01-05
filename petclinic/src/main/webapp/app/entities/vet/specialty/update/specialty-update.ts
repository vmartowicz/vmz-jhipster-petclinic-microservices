import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { VetService } from 'app/entities/vet/vet/service/vet.service';
import { IVet } from 'app/entities/vet/vet/vet.model';
import SharedModule from 'app/shared/shared.module';
import { SpecialtyService } from '../service/specialty.service';
import { ISpecialty } from '../specialty.model';

import { SpecialtyFormGroup, SpecialtyFormService } from './specialty-form.service';

@Component({
  selector: 'jhi-specialty-update',
  templateUrl: './specialty-update.html',
  imports: [SharedModule, ReactiveFormsModule],
})
export class SpecialtyUpdate implements OnInit {
  isSaving = false;
  specialty: ISpecialty | null = null;

  vetsSharedCollection = signal<IVet[]>([]);

  protected specialtyService = inject(SpecialtyService);
  protected specialtyFormService = inject(SpecialtyFormService);
  protected vetService = inject(VetService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SpecialtyFormGroup = this.specialtyFormService.createSpecialtyFormGroup();

  compareVet = (o1: IVet | null, o2: IVet | null): boolean => this.vetService.compareVet(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ specialty }) => {
      this.specialty = specialty;
      if (specialty) {
        this.updateForm(specialty);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving = true;
    const specialty = this.specialtyFormService.getSpecialty(this.editForm);
    if (specialty.id === null) {
      this.subscribeToSaveResponse(this.specialtyService.create(specialty));
    } else {
      this.subscribeToSaveResponse(this.specialtyService.update(specialty));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpecialty>>): void {
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
    this.isSaving = false;
  }

  protected updateForm(specialty: ISpecialty): void {
    this.specialty = specialty;
    this.specialtyFormService.resetForm(this.editForm, specialty);

    this.vetsSharedCollection.set(
      this.vetService.addVetToCollectionIfMissing<IVet>(this.vetsSharedCollection(), ...(specialty.vets ?? [])),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.vetService
      .query()
      .pipe(map((res: HttpResponse<IVet[]>) => res.body ?? []))
      .pipe(map((vets: IVet[]) => this.vetService.addVetToCollectionIfMissing<IVet>(vets, ...(this.specialty?.vets ?? []))))
      .subscribe((vets: IVet[]) => this.vetsSharedCollection.set(vets));
  }
}
