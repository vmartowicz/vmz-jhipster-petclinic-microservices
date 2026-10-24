import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IVet } from '../vet.model';

@Component({
  selector: 'jhi-vet-detail',
  templateUrl: './vet-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class VetDetail {
  readonly vet = input<IVet | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
