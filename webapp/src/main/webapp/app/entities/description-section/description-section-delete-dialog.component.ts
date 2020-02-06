import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDescriptionSection } from 'app/shared/model/description-section.model';
import { DescriptionSectionService } from './description-section.service';

@Component({
  selector: 'jhi-description-section-delete-dialog',
  templateUrl: './description-section-delete-dialog.component.html'
})
export class DescriptionSectionDeleteDialogComponent {
  descriptionSection: IDescriptionSection;

  constructor(
    protected descriptionSectionService: DescriptionSectionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.descriptionSectionService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'descriptionSectionListModification',
        content: 'Deleted an descriptionSection'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-description-section-delete-popup',
  template: ''
})
export class DescriptionSectionDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ descriptionSection }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DescriptionSectionDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.descriptionSection = descriptionSection;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/description-section', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/description-section', { outlets: { popup: null } }]);
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
