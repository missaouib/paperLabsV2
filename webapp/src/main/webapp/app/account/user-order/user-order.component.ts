import { Account } from 'app/core/user/account.model';
import { Component, OnInit } from '@angular/core';
import { IOrder } from 'app/shared/model/order.model';
import { UserOrderService } from 'app/account/user-order/user-order.service';
import { AccountService } from 'app/core/auth/account.service';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiParseLinks } from 'ng-jhipster';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { HttpHeaders } from '@angular/common/http';
import { SearchUtil } from 'app/shared/util/search-util';
import { StepperEventManagerService } from 'app/stepper/stepper/stepper-event-manager.service';
import { StepperDataService } from 'app/stepper/stepper/stepper-data.service';

@Component({
  selector: 'jhi-user-order',
  templateUrl: './user-order.component.html',
  styleUrls: ['./user-order.component.scss']
})
export class UserOrderComponent implements OnInit {
  ordersByUserId: IOrder[];
  page: any;
  routeData: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  totalItems: any;
  itemsPerPage: any;
  links: any;
  currentSearch: string;
  ordersByUserIdCopy: IOrder[];
  userLogin: string;
  order: IOrder;

  constructor(
    private stepperEventManagerService: StepperEventManagerService,
    private userOrderService: UserOrderService,
    private accountService: AccountService,
    private stepperDataService: StepperDataService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected parseLinks: JhiParseLinks
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }

  protected paginateOrders(data: IOrder[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.ordersByUserId = data;
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }
  transition() {
    this.router.navigate(['/account/profile/orders'], {
      queryParams: {
        userLogin: this.userLogin,
        page: this.page,
        size: this.itemsPerPage
      }
    });
    this.ngOnInit();
  }

  paramChanged($event: string[]) {
    this.ordersByUserIdCopy = this.ordersByUserId.map(obj => ({ ...obj }));
    if ($event.length !== 0) {
      if (this.currentSearch.length !== 0) {
        this.ordersByUserIdCopy = SearchUtil.filterArrayByString(this.ordersByUserIdCopy, this.currentSearch);
      }
    }
  }

  ngOnInit() {
    if (this.accountService.isAuthenticated()) {
      this.accountService.identity().then((account: Account) => {
        if (account !== null) {
          this.userLogin = account.login;
          this.userOrderService.getAllOrdersByUserLogin(account.login, this.page - 1, this.itemsPerPage).subscribe(res => {
            this.ordersByUserIdCopy = res.body;
            this.paginateOrders(res.body, res.headers);
          });
        }
      });
    }
  }

  downloadPDF(filePath: string, orderStatus: string, order: IOrder) {
    if (orderStatus === 'PAID') {
      this.stepperDataService.downloadPDF(order);
    } else if (orderStatus === 'WAITING') {
      this.stepperEventManagerService.sendOrder(order);
      this.router.navigate(['account/profile/orders', order.id, 'payment']);
    }
  }

  transformOrderIdentifier(orderIdentifier: string): string {
    return this.userOrderService.transformOrderIdentifier(orderIdentifier);
  }
}
