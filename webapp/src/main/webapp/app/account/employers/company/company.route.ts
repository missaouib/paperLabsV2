import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { CompanyComponent } from 'app/account/employers/company/company.component';
import { Injectable } from '@angular/core';
import { Order } from 'app/shared/model/order.model';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { Company, ICompany } from 'app/shared/model/company.model';
import { CompanyService } from 'app/account/employers/company/company.service';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { CompanyDetailComponent } from 'app/account/employers/company/company-detail.component';

@Injectable({ providedIn: 'root' })
export class CompanyResolve implements Resolve<ICompany> {
  constructor(private service: CompanyService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICompany> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Company>) => response.ok),
        map((company: HttpResponse<Order>) => company.body)
      );
    }
    return of(new Company());
  }
}
export const companyRoute: Routes = [
  {
    path: 'company',
    component: CompanyComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'paperlabsApp.user-companies.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'company/:id/view',
    component: CompanyDetailComponent,
    resolve: {
      company: CompanyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.user-companies.details'
    },
    canActivate: [UserRouteAccessService]
  }
];
