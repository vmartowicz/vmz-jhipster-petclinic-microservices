import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IOwner } from '../owner.model';

@Component({
  selector: 'jhi-owner-detail',
  templateUrl: './owner-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class OwnerDetail {
  readonly owner = input<IOwner | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
