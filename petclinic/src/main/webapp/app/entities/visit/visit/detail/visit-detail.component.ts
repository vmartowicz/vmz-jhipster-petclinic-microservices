import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IVisit } from '../visit.model';

@Component({
  selector: 'jhi-visit-detail',
  templateUrl: './visit-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class VisitDetailComponent {
  visit = input<IVisit | null>(null);

  previousState(): void {
    window.history.back();
  }
}
