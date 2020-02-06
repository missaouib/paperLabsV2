import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrder } from 'app/shared/model/order.model';
import { OrderService } from 'app/entities/order/order.service';

@Component({
  selector: 'jhi-order-detail',
  templateUrl: './order-detail.component.html'
})
export class OrderDetailComponent implements OnInit {
  order: IOrder;

  constructor(protected activatedRoute: ActivatedRoute, protected orderService: OrderService) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ order }) => {
      this.order = order;
    });
  }

  transformOrderIdentifier(orderIdentifier: string): string {
    return this.orderService.transformOrderIdentifier(orderIdentifier);
  }

  previousState() {
    window.history.back();
  }
}
