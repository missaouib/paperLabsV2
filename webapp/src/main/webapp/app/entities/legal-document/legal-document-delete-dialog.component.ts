import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILegalDocument } from 'app/shared/model/legal-document.model';
import { LegalDocumentService } from './legal-document.service';

@Component({
  selector: 'jhi-legal-document-delete-dialog',
  templateUrl: './legal-document-delete-dialog.component.html'
})
export class LegalDocumentDeleteDialogComponent {
  legalDocument: ILegalDocument;

  constructor(
    protected legalDocumentService: LegalDocumentService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.legalDocumentService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'legalDocumentListModification',
        content: 'Deleted an legalDocument'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-legal-document-delete-popup',
  template: ''
})
export class LegalDocumentDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ legalDocument }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(LegalDocumentDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.legalDocument = legalDocument;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/legal-document', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/legal-document', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
