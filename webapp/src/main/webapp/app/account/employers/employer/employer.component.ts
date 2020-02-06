import { Component, OnInit } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiParseLinks } from 'ng-jhipster';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { HttpHeaders } from '@angular/common/http';
import { SearchUtil } from 'app/shared/util/search-util';
import { Account } from 'app/core/user/account.model';
import { IEmployer } from 'app/shared/model/employer.model';
import { EmployerService } from 'app/account/employers/employer/employer.service';

@Component({
  selector: 'jhi-employer',
  templateUrl: './employer.component.html',
  styleUrls: ['./employer.component.scss']
})
export class EmployerComponent implements OnInit {
  employersByUserId: IEmployer[];
  page: any;
  routeData: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  totalItems: any;
  itemsPerPage: any;
  links: any;
  currentSearch: string;
  employersByUserIdCopy: IEmployer[];
  userLogin: string;

  constructor(
    private employerService: EmployerService,
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

  protected paginateEmployers(data: IEmployer[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    this.employersByUserId = data;
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
    this.employersByUserIdCopy = this.employersByUserId.map(obj => ({ ...obj }));
    if ($event.length !== 0) {
      if (this.currentSearch.length !== 0) {
        this.employersByUserIdCopy = SearchUtil.filterArrayByString(this.employersByUserIdCopy, this.currentSearch);
      }
    }
  }

  ngOnInit() {
    if (this.accountService.isAuthenticated()) {
      this.accountService.identity().then((account: Account) => {
        if (account !== null) {
          this.userLogin = account.login;
          this.employerService.searchEmployersByUser(account.login, this.page - 1, this.itemsPerPage).subscribe(res => {
            this.employersByUserIdCopy = res.body;
            this.paginateEmployers(res.body, res.headers);
          });
        }
      });
    }
  }
}
