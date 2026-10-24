import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ISpecialty } from '../specialty.model';

@Component({
  selector: 'jhi-specialty-detail',
  templateUrl: './specialty-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class SpecialtyDetail {
  readonly specialty = input<ISpecialty | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
