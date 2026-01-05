import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IVet } from '../vet.model';

@Component({
  selector: 'jhi-vet-detail',
  templateUrl: './vet-detail.html',
  imports: [SharedModule, RouterLink],
})
export class VetDetail {
  vet = input<IVet | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
