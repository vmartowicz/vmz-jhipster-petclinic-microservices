import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FormatMediumDatePipe } from 'app/shared/date';
import SharedModule from 'app/shared/shared.module';
import { IVisit } from '../visit.model';

@Component({
  selector: 'jhi-visit-detail',
  templateUrl: './visit-detail.html',
  imports: [SharedModule, RouterLink, FormatMediumDatePipe],
})
export class VisitDetail {
  visit = input<IVisit | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
