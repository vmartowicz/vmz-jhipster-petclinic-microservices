import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IOwner } from '../owner.model';

@Component({
  selector: 'jhi-owner-detail',
  templateUrl: './owner-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class OwnerDetailComponent {
  owner = input<IOwner | null>(null);

  previousState(): void {
    window.history.back();
  }
}
