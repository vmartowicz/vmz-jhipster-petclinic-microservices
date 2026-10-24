import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISpecialty, NewSpecialty } from '../specialty.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISpecialty for edit and NewSpecialtyFormGroupInput for create.
 */
type SpecialtyFormGroupInput = ISpecialty | PartialWithRequiredKeyOf<NewSpecialty>;

type SpecialtyFormDefaults = Pick<NewSpecialty, 'id' | 'vetses'>;

type SpecialtyFormGroupContent = {
  id: FormControl<ISpecialty['id'] | NewSpecialty['id']>;
  name: FormControl<ISpecialty['name']>;
  vetses: FormControl<ISpecialty['vetses']>;
};

export type SpecialtyFormGroup = FormGroup<SpecialtyFormGroupContent>;

@Service()
export class SpecialtyFormService {
  createSpecialtyFormGroup(specialty?: SpecialtyFormGroupInput): SpecialtyFormGroup {
    const specialtyRawValue = {
      ...this.getFormDefaults(),
      ...(specialty ?? { id: null }),
    };

    return new FormGroup<SpecialtyFormGroupContent>({
      id: new FormControl(
        { value: specialtyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(specialtyRawValue.name, {
        validators: [Validators.required, Validators.maxLength(80)],
      }),
      vetses: new FormControl(specialtyRawValue.vetses ?? []),
    });
  }

  getSpecialty(form: SpecialtyFormGroup): ISpecialty | NewSpecialty {
    return form.getRawValue();
  }

  resetForm(form: SpecialtyFormGroup, specialty: SpecialtyFormGroupInput): void {
    const specialtyRawValue = { ...this.getFormDefaults(), ...specialty };
    form.reset({
      ...specialtyRawValue,
      id: { value: specialtyRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): SpecialtyFormDefaults {
    return {
      id: null,
      vetses: [],
    };
  }
}
