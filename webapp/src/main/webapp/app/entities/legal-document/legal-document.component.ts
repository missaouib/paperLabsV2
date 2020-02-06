import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import { ILegalDocument } from 'app/shared/model/legal-document.model';
import { AccountService } from 'app/core/auth/account.service';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { LegalDocumentService } from './legal-document.service';

@Component({
  selector: 'jhi-legal-document',
  templateUrl: './legal-document.component.html'
})
export class LegalDocumentComponent implements OnInit, OnDestroy {
  currentAccount: any;
  legalDocuments: ILegalDocument[];
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
  totalRevenuesDesDocumentsJuridiques = 0;
  totalDownloadsOfAllDocuments = 0;
  allLegalDocuments: ILegalDocument[];
  numberOfLegalDocuments: number;

  constructor(
    protected legalDocumentService: LegalDocumentService,
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

  ngOnInit() {
    this.loadAllLegalDocuments();
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInLegalDocuments();
  }

  loadAllLegalDocuments() {
    this.legalDocumentService.getAllLegalDocuments().subscribe(
      (res: HttpResponse<ILegalDocument[]>) => {
        this.allLegalDocuments = res.body;
        this.numberOfLegalDocuments = res.body.length;
        this.getTotalDownloadsOfAllDocuments(res.body);
        this.getTotalRevenuesDesDocumentsJuridiques(res.body);
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  loadAll() {
    if (this.currentSearch) {
      this.legalDocumentService
        .search({
          page: this.page - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: this.sort()
        })
        .subscribe(
          (res: HttpResponse<ILegalDocument[]>) => this.paginateLegalDocuments(res.body, res.headers),
          (res: HttpErrorResponse) => this.onError(res.message)
        );
      return;
    }
    this.legalDocumentService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<ILegalDocument[]>) => this.paginateLegalDocuments(res.body, res.headers),
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
    this.router.navigate(['/legal-document'], {
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
      '/legal-document',
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
      '/legal-document',
      {
        search: this.currentSearch,
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    ]);
    this.loadAll();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ILegalDocument) {
    return item.id;
  }

  registerChangeInLegalDocuments() {
    this.eventSubscriber = this.eventManager.subscribe('legalDocumentListModification', response => this.loadAll());
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateLegalDocuments(data: ILegalDocument[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.legalDocuments = data;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  getTotalRevenuesDesDocumentsJuridiques(allLegalDocuments: ILegalDocument[]) {
    allLegalDocuments.forEach(doc => {
      this.legalDocumentService.getDocumentStatisticsInfo(doc.id).subscribe(info => {
        this.totalRevenuesDesDocumentsJuridiques += info.body.sumDocumentRevenue;
      });
    });
  }

  getTotalDownloadsOfAllDocuments(allLegalDocuments: ILegalDocument[]) {
    allLegalDocuments.forEach(doc => {
      this.legalDocumentService.getDocumentStatisticsInfo(doc.id).subscribe(info => {
        this.totalDownloadsOfAllDocuments += info.body.numberOfDownload;
      });
    });
  }
}
