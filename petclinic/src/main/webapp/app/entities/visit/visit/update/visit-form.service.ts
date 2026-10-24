import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVisit, NewVisit } from '../visit.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVisit for edit and NewVisitFormGroupInput for create.
 */
type VisitFormGroupInput = IVisit | PartialWithRequiredKeyOf<NewVisit>;

type VisitFormDefaults = Pick<NewVisit, 'id'>;

type VisitFormGroupContent = {
  id: FormControl<IVisit['id'] | NewVisit['id']>;
  visitDate: FormControl<IVisit['visitDate']>;
  description: FormControl<IVisit['description']>;
  petId: FormControl<IVisit['petId']>;
};

export type VisitFormGroup = FormGroup<VisitFormGroupContent>;

@Service()
export class VisitFormService {
  createVisitFormGroup(visit?: VisitFormGroupInput): VisitFormGroup {
    const visitRawValue = {
      ...this.getFormDefaults(),
      ...(visit ?? { id: null }),
    };

    return new FormGroup<VisitFormGroupContent>({
      id: new FormControl(
        { value: visitRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      visitDate: new FormControl(visitRawValue.visitDate),
      description: new FormControl(visitRawValue.description, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      petId: new FormControl(visitRawValue.petId, {
        validators: [Validators.required],
      }),
    });
  }

  getVisit(form: VisitFormGroup): IVisit | NewVisit {
    return form.getRawValue();
  }

  resetForm(form: VisitFormGroup, visit: VisitFormGroupInput): void {
    const visitRawValue = { ...this.getFormDefaults(), ...visit };
    form.reset({
      ...visitRawValue,
      id: { value: visitRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): VisitFormDefaults {
    return {
      id: null,
    };
  }
}
