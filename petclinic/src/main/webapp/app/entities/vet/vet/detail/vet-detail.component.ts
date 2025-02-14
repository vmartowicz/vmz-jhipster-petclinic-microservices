import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IVet } from '../vet.model';

@Component({
  selector: 'jhi-vet-detail',
  templateUrl: './vet-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class VetDetailComponent {
  vet = input<IVet | null>(null);

  previousState(): void {
    window.history.back();
  }
}
