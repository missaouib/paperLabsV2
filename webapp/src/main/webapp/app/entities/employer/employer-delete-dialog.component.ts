import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEmployer } from 'app/shared/model/employer.model';
import { EmployerService } from './employer.service';

@Component({
  selector: 'jhi-employer-delete-dialog',
  templateUrl: './employer-delete-dialog.component.html'
})
export class EmployerDeleteDialogComponent {
  employer: IEmployer;

  constructor(protected employerService: EmployerService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.employerService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'employerListModification',
        content: 'Deleted an employer'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-employer-delete-popup',
  template: ''
})
export class EmployerDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ employer }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(EmployerDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.employer = employer;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/employer', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/employer', { outlets: { popup: null } }]);
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
