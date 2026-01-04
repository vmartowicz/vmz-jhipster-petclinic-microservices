import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IOwner } from '../owner.model';

@Component({
  selector: 'jhi-owner-detail',
  templateUrl: './owner-detail.html',
  imports: [SharedModule, RouterLink],
})
export class OwnerDetail {
  owner = input<IOwner | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
