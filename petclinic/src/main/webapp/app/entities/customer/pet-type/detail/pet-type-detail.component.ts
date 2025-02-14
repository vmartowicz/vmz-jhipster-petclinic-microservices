import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IPetType } from '../pet-type.model';

@Component({
  selector: 'jhi-pet-type-detail',
  templateUrl: './pet-type-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class PetTypeDetailComponent {
  petType = input<IPetType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
