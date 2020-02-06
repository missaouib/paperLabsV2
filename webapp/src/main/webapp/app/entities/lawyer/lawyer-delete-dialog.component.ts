import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILawyer } from 'app/shared/model/lawyer.model';
import { LawyerService } from './lawyer.service';

@Component({
  selector: 'jhi-lawyer-delete-dialog',
  templateUrl: './lawyer-delete-dialog.component.html'
})
export class LawyerDeleteDialogComponent {
  lawyer: ILawyer;

  constructor(protected lawyerService: LawyerService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.lawyerService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'lawyerListModification',
        content: 'Deleted an lawyer'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-lawyer-delete-popup',
  template: ''
})
export class LawyerDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ lawyer }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(LawyerDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.lawyer = lawyer;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/lawyer', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/lawyer', { outlets: { popup: null } }]);
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
