import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVet, NewVet } from '../vet.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVet for edit and NewVetFormGroupInput for create.
 */
type VetFormGroupInput = IVet | PartialWithRequiredKeyOf<NewVet>;

type VetFormDefaults = Pick<NewVet, 'id' | 'specialtieses'>;

type VetFormGroupContent = {
  id: FormControl<IVet['id'] | NewVet['id']>;
  firstName: FormControl<IVet['firstName']>;
  lastName: FormControl<IVet['lastName']>;
  specialtieses: FormControl<IVet['specialtieses']>;
};

export type VetFormGroup = FormGroup<VetFormGroupContent>;

@Service()
export class VetFormService {
  createVetFormGroup(vet?: VetFormGroupInput): VetFormGroup {
    const vetRawValue = {
      ...this.getFormDefaults(),
      ...(vet ?? { id: null }),
    };

    return new FormGroup<VetFormGroupContent>({
      id: new FormControl(
        { value: vetRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstName: new FormControl(vetRawValue.firstName, {
        validators: [Validators.required, Validators.maxLength(30)],
      }),
      lastName: new FormControl(vetRawValue.lastName, {
        validators: [Validators.required, Validators.maxLength(30)],
      }),
      specialtieses: new FormControl(vetRawValue.specialtieses ?? []),
    });
  }

  getVet(form: VetFormGroup): IVet | NewVet {
    return form.getRawValue();
  }

  resetForm(form: VetFormGroup, vet: VetFormGroupInput): void {
    const vetRawValue = { ...this.getFormDefaults(), ...vet };
    form.reset({
      ...vetRawValue,
      id: { value: vetRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): VetFormDefaults {
    return {
      id: null,
      specialtieses: [],
    };
  }
}
