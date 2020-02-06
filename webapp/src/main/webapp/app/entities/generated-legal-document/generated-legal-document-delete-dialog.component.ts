import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGeneratedLegalDocument } from 'app/shared/model/generated-legal-document.model';
import { GeneratedLegalDocumentService } from './generated-legal-document.service';

@Component({
  selector: 'jhi-generated-legal-document-delete-dialog',
  templateUrl: './generated-legal-document-delete-dialog.component.html'
})
export class GeneratedLegalDocumentDeleteDialogComponent {
  generatedLegalDocument: IGeneratedLegalDocument;

  constructor(
    protected generatedLegalDocumentService: GeneratedLegalDocumentService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.generatedLegalDocumentService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'generatedLegalDocumentListModification',
        content: 'Deleted an generatedLegalDocument'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-generated-legal-document-delete-popup',
  template: ''
})
export class GeneratedLegalDocumentDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ generatedLegalDocument }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(GeneratedLegalDocumentDeleteDialogComponent as Component, {
          size: 'lg',
          backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.generatedLegalDocument = generatedLegalDocument;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/generated-legal-document', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/generated-legal-document', { outlets: { popup: null } }]);
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
