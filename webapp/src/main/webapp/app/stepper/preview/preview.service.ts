import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { PreviewModalComponent } from 'app/stepper/preview/preview-modal/preview-modal.component';

@Injectable({ providedIn: 'root' })
export class PreviewModalService {
  private isOpen = false;
  constructor(private modalService: NgbModal) {}

  open(): NgbModalRef {
    if (this.isOpen) {
      return;
    }
    this.isOpen = true;
    const modalRef = this.modalService.open(PreviewModalComponent);
    modalRef.result.then(
      result => {
        this.isOpen = false;
      },
      reason => {
        this.isOpen = false;
      }
    );
    return modalRef;
  }
}
