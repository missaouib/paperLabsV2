import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IEmployer } from 'app/shared/model/employer.model';
import { AccountService } from 'app/core/auth/account.service';
import { EmployerService } from './employer.service';

@Component({
  selector: 'jhi-employer',
  templateUrl: './employer.component.html'
})
export class EmployerComponent implements OnInit, OnDestroy {
  employers: IEmployer[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected employerService: EmployerService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll() {
    if (this.currentSearch) {
      this.employerService
        .search({
          query: this.currentSearch
        })
        .pipe(
          filter((res: HttpResponse<IEmployer[]>) => res.ok),
          map((res: HttpResponse<IEmployer[]>) => res.body)
        )
        .subscribe((res: IEmployer[]) => (this.employers = res), (res: HttpErrorResponse) => this.onError(res.message));
      return;
    }
    this.employerService
      .query()
      .pipe(
        filter((res: HttpResponse<IEmployer[]>) => res.ok),
        map((res: HttpResponse<IEmployer[]>) => res.body)
      )
      .subscribe(
        (res: IEmployer[]) => {
          this.employers = res;
          this.currentSearch = '';
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  search(query) {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.loadAll();
  }

  clear() {
    this.currentSearch = '';
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInEmployers();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IEmployer) {
    return item.id;
  }

  registerChangeInEmployers() {
    this.eventSubscriber = this.eventManager.subscribe('employerListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
