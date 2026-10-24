import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IPet } from '../pet.model';

@Component({
  selector: 'jhi-pet-detail',
  templateUrl: './pet-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class PetDetail {
  readonly pet = input<IPet | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
