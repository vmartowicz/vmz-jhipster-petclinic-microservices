import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ISpecialty } from '../specialty.model';

@Component({
  selector: 'jhi-specialty-detail',
  templateUrl: './specialty-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class SpecialtyDetailComponent {
  specialty = input<ISpecialty | null>(null);

  previousState(): void {
    window.history.back();
  }
}
