import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IPet } from '../pet.model';

@Component({
  selector: 'jhi-pet-detail',
  templateUrl: './pet-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatePipe],
})
export class PetDetail {
  pet = input<IPet | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
