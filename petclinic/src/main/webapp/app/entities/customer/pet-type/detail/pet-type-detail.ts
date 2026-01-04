import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IPetType } from '../pet-type.model';

@Component({
  selector: 'jhi-pet-type-detail',
  templateUrl: './pet-type-detail.html',
  imports: [SharedModule, RouterLink],
})
export class PetTypeDetail {
  petType = input<IPetType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
