import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDowloadHistory } from 'app/shared/model/dowload-history.model';
import { DowloadHistoryService } from './dowload-history.service';

@Component({
  selector: 'jhi-dowload-history-delete-dialog',
  templateUrl: './dowload-history-delete-dialog.component.html'
})
export class DowloadHistoryDeleteDialogComponent {
  dowloadHistory: IDowloadHistory;

  constructor(
    protected dowloadHistoryService: DowloadHistoryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.dowloadHistoryService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'dowloadHistoryListModification',
        content: 'Deleted an dowloadHistory'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-dowload-history-delete-popup',
  template: ''
})
export class DowloadHistoryDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ dowloadHistory }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DowloadHistoryDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.dowloadHistory = dowloadHistory;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/dowload-history', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/dowload-history', { outlets: { popup: null } }]);
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
