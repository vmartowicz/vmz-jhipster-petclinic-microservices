import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVet } from '../vet.model';
import { VetService } from '../service/vet.service';

@Component({
  templateUrl: './vet-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VetDeleteDialogComponent {
  vet?: IVet;

  protected vetService = inject(VetService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vetService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
