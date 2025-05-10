import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOwner } from '../owner.model';
import { OwnerService } from '../service/owner.service';

@Component({
  templateUrl: './owner-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OwnerDeleteDialogComponent {
  owner?: IOwner;

  protected ownerService = inject(OwnerService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ownerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
