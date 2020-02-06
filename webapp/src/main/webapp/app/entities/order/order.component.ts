import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IOrder } from 'app/shared/model/order.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { OrderService } from './order.service';

@Component({
  selector: 'jhi-order',
  templateUrl: './order.component.html',
  styleUrls: ['./orders.component.scss']
})
export class OrderComponent implements OnInit, OnDestroy {
  currentAccount: any;
  orders: IOrder[];
  error: any;
  success: any;
  eventSubscriber: Subscription;
  currentSearch: string;
  routeData: any;
  links: any;
  totalItems: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  waitingOrderToday = 0;
  paidOrderToday = 0;
  abandonedOrderToday = 0;

  constructor(
    protected orderService: OrderService,
    protected parseLinks: JhiParseLinks,
    protected jhiAlertService: JhiAlertService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll() {
    if (this.currentSearch) {
      this.orderService
        .search({
          page: this.page - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: this.sort()
        })
        .subscribe(
          (res: HttpResponse<IOrder[]>) => this.paginateOrders(res.body, res.headers),
          (res: HttpErrorResponse) => this.onError(res.message)
        );
      return;
    }
    this.orderService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IOrder[]>) => this.paginateOrders(res.body, res.headers),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/order'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        search: this.currentSearch,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  clear() {
    this.page = 0;
    this.currentSearch = '';
    this.router.navigate([
      '/order',
      {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  search(query) {
    if (!query) {
      return this.clear();
    }
    this.page = 0;
    this.currentSearch = query;
    this.router.navigate([
      '/order',
      {
        search: this.currentSearch,
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInOrders();
    this.getWaitingOrderToday();
    this.getPaidOrderToday();
    this.getAbandonedOrderToday();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IOrder) {
    return item.id;
  }

  registerChangeInOrders() {
    this.eventSubscriber = this.eventManager.subscribe('orderListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  getOrderClassByStatus(orderStatus: string): string {
    if (orderStatus === 'WAITING') return 'bg_yellow';
    else if (orderStatus === 'PAID') return 'bg_green';
    else if (orderStatus === 'ABANDONED') return 'bg_red';
    else return '';
  }

  protected paginateOrders(data: IOrder[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.orders = data;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  getWaitingOrderToday() {
    this.orderService.getOrdersStatistics().subscribe(res => {
      this.waitingOrderToday = res.body.orderStatistics.WaitingOrderToday as number;
    });
  }

  getPaidOrderToday() {
    this.orderService.getOrdersStatistics().subscribe(res => {
      this.paidOrderToday = res.body.orderStatistics.PaidOrderToday as number;
    });
  }

  getAbandonedOrderToday() {
    this.orderService.getOrdersStatistics().subscribe(res => {
      this.abandonedOrderToday = res.body.orderStatistics.AbandonedOrderToday as number;
    });
  }

  transformOrderIdentifier(orderIdentifier: string): string {
    return this.orderService.transformOrderIdentifier(orderIdentifier);
  }
}
