import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { ActivateService } from './activate.service';

@Component({
  selector: 'jhi-activate',
  templateUrl: './activate.component.html'
})
export class ActivateComponent implements OnInit {
  error: string;
  success: string;
  modalRef: NgbModalRef;

  constructor(
    private activateService: ActivateService,
    private loginModalService: LoginModalService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.activateService.get(params['key'], params['orderId']).subscribe(
        () => {
          this.error = null;
          this.success = 'CDI';
          if (params['orderId'] !== null && params['orderId'] !== undefined) {
            this.router.navigate([`/account/profile/orders/${params['orderId']}/view`, { afterActivation: true }]);
          }
        },
        () => {
          this.success = null;
          this.error = 'ERROR';
        }
      );
    });
  }

  login() {
    this.modalRef = this.loginModalService.open();
  }
}
