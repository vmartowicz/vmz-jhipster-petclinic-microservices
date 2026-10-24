import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IVisit } from '../visit.model';

@Component({
  selector: 'jhi-visit-detail',
  templateUrl: './visit-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class VisitDetail {
  readonly visit = input<IVisit | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
