import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { Observable, finalize, map } from 'rxjs';

import { IOwner } from 'app/entities/customer/owner/owner.model';
import { OwnerService } from 'app/entities/customer/owner/service/owner.service';
import { IPetType } from 'app/entities/customer/pet-type/pet-type.model';
import { PetTypeService } from 'app/entities/customer/pet-type/service/pet-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPet } from '../pet.model';
import { PetService } from '../service/pet.service';

import { PetFormGroup, PetFormService } from './pet-form.service';

@Component({
  selector: 'jhi-pet-update',
  templateUrl: './pet-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class PetUpdate implements OnInit {
  readonly isSaving = signal(false);
  pet: IPet | null = null;

  petTypesSharedCollection = signal<IPetType[]>([]);
  ownersSharedCollection = signal<IOwner[]>([]);

  protected petService = inject(PetService);
  protected petFormService = inject(PetFormService);
  protected petTypeService = inject(PetTypeService);
  protected ownerService = inject(OwnerService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PetFormGroup = this.petFormService.createPetFormGroup();

  comparePetType = (o1: IPetType | null, o2: IPetType | null): boolean => this.petTypeService.comparePetType(o1, o2);

  compareOwner = (o1: IOwner | null, o2: IOwner | null): boolean => this.ownerService.compareOwner(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pet }) => {
      this.pet = pet;
      if (pet) {
        this.updateForm(pet);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const pet = this.petFormService.getPet(this.editForm);
    if (pet.id === null) {
      this.subscribeToSaveResponse(this.petService.create(pet));
    } else {
      this.subscribeToSaveResponse(this.petService.update(pet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPet | null>): void {
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

  protected updateForm(pet: IPet): void {
    this.pet = pet;
    this.petFormService.resetForm(this.editForm, pet);

    this.petTypesSharedCollection.update(petTypes => this.petTypeService.addPetTypeToCollectionIfMissing<IPetType>(petTypes, pet.type));
    this.ownersSharedCollection.update(owners => this.ownerService.addOwnerToCollectionIfMissing<IOwner>(owners, pet.owner));
  }

  protected loadRelationshipsOptions(): void {
    this.petTypeService
      .query()
      .pipe(map((res: HttpResponse<IPetType[]>) => res.body ?? []))
      .pipe(map((petTypes: IPetType[]) => this.petTypeService.addPetTypeToCollectionIfMissing<IPetType>(petTypes, this.pet?.type)))
      .subscribe((petTypes: IPetType[]) => this.petTypesSharedCollection.set(petTypes));

    this.ownerService
      .query()
      .pipe(map((res: HttpResponse<IOwner[]>) => res.body ?? []))
      .pipe(map((owners: IOwner[]) => this.ownerService.addOwnerToCollectionIfMissing<IOwner>(owners, this.pet?.owner)))
      .subscribe((owners: IOwner[]) => this.ownersSharedCollection.set(owners));
  }
}
