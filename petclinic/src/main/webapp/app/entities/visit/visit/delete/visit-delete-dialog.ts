import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import SharedModule from 'app/shared/shared.module';
import { VisitService } from '../service/visit.service';
import { IVisit } from '../visit.model';

@Component({
  templateUrl: './visit-delete-dialog.html',
  imports: [SharedModule, FormsModule],
})
export class VisitDeleteDialog {
  visit?: IVisit;

  protected visitService = inject(VisitService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.visitService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
