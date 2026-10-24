import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPetType } from '../pet-type.model';

@Component({
  selector: 'jhi-pet-type-detail',
  templateUrl: './pet-type-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class PetTypeDetail {
  readonly petType = input<IPetType | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
