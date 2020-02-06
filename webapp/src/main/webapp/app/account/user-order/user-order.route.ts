import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { UserOrderComponent } from 'app/account/user-order/user-order.component';
import { UserOrderDetailComponent } from 'app/account/user-order/user-order-detail.component';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot, Routes } from '@angular/router';
import { Injectable } from '@angular/core';
import { IOrder, Order } from 'app/shared/model/order.model';
import { of } from 'rxjs';
import { PaymentStepComponent } from 'app/stepper/payment-step/payment-step.component';
import { UserOrderService } from 'app/account/user-order/user-order.service';
import { StepperEventManagerService } from 'app/stepper/stepper/stepper-event-manager.service';

@Injectable({ providedIn: 'root' })
export class UserOrderResolve implements Resolve<IOrder> {
  constructor(private service: UserOrderService, private router: Router, private stepperEventManagerService: StepperEventManagerService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): any {
    const id = route.params['id'];
    if (id) {
      this.service.findByAuthenticatedUser(id).subscribe(
        res => {
          this.stepperEventManagerService.sendOrder(res.body);
        },
        () => {
          this.router.navigate([`/account/profile/orders`]);
        }
      );
    } else {
      return of(new Order());
    }
  }
}

export const userOrderRoute: Routes = [
  {
    path: 'orders',
    component: UserOrderComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'paperlabsApp.user-orders.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'orders/:id/view',
    component: UserOrderDetailComponent,
    resolve: {
      order: UserOrderResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.user-orders.details'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'orders/:orderId/payment',
    component: PaymentStepComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.user-orders.payment'
    },
    canActivate: [UserRouteAccessService]
  }
];
