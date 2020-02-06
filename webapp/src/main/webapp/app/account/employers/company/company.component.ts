import { Component, OnInit } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiParseLinks } from 'ng-jhipster';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { HttpHeaders } from '@angular/common/http';
import { SearchUtil } from 'app/shared/util/search-util';
import { Account } from 'app/core/user/account.model';
import { ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/account/employers/company/company.service';

@Component({
  selector: 'jhi-company',
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.scss']
})
export class CompanyComponent implements OnInit {
  companiesByUserId: ICompany[];
  page: any;
  routeData: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  totalItems: any;
  itemsPerPage: any;
  links: any;
  currentSearch: string;
  companiesByUserIdCopy: ICompany[];
  userLogin: string;

  constructor(
    private companyService: CompanyService,
    private accountService: AccountService,
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

  protected paginateCompanies(data: ICompany[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.companiesByUserId = data;
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/account/profile/employers'], {
      queryParams: {
        userLogin: this.userLogin,
        page: this.page,
        size: this.itemsPerPage
      }
    });
    this.ngOnInit();
  }

  paramChanged($event: string[]) {
    this.companiesByUserIdCopy = this.companiesByUserId.map(obj => ({ ...obj }));
    if ($event.length !== 0) {
      if (this.currentSearch.length !== 0) {
        this.companiesByUserIdCopy = SearchUtil.filterArrayByString(this.companiesByUserIdCopy, this.currentSearch);
      }
    }
  }

  ngOnInit() {
    if (this.accountService.isAuthenticated()) {
      this.accountService.identity().then((account: Account) => {
        if (account !== null) {
          this.userLogin = account.login;
          this.companyService.searchCompaniesByUser(account.login, this.page - 1, this.itemsPerPage).subscribe(res => {
            this.companiesByUserIdCopy = res.body;
            this.paginateCompanies(res.body, res.headers);
          });
        }
      });
    }
  }
}
