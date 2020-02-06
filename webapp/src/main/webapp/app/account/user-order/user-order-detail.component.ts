import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrder } from 'app/shared/model/order.model';
import { Subscription } from 'rxjs';
import { StepperEventManagerService } from 'app/stepper/stepper/stepper-event-manager.service';
import { UserOrderService } from 'app/account/user-order/user-order.service';

@Component({
  selector: 'jhi-user-order-detail',
  templateUrl: './user-order-detail.component.html'
})
export class UserOrderDetailComponent implements OnInit, OnDestroy {
  order: IOrder;
  orderSubscriber: Subscription;

  constructor(
    protected activatedRoute: ActivatedRoute,
    private stepperEventManagerService: StepperEventManagerService,
    private userOrderService: UserOrderService
  ) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(() => {
      this.orderSubscriber = this.stepperEventManagerService.getOrder().subscribe(order => {
        this.order = order;
      });
    });
  }

  transformOrderIdentifier(orderIdentifier: string): string {
    return this.userOrderService.transformOrderIdentifier(orderIdentifier);
  }

  previousState() {
    window.history.back();
  }

  ngOnDestroy(): void {
    this.orderSubscriber.unsubscribe();
  }
}
