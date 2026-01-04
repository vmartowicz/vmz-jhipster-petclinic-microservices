import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ISpecialty } from '../specialty.model';

@Component({
  selector: 'jhi-specialty-detail',
  templateUrl: './specialty-detail.html',
  imports: [SharedModule, RouterLink],
})
export class SpecialtyDetail {
  specialty = input<ISpecialty | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
