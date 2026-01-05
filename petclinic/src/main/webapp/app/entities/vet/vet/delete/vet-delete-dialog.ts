import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { VetService } from '../service/vet.service';
import { IVet } from '../vet.model';

@Component({
  templateUrl: './vet-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class VetDeleteDialog {
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
