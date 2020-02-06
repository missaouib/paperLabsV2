import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { EmployersComponent } from 'app/account/employers/employers/employers.component';
import { companyRoute } from 'app/account/employers/company/company.route';
import { employerRoute } from 'app/account/employers/employer/employer.route';
import { JhiResolvePagingParams } from 'ng-jhipster';

export const employersRoute: Routes = [
  {
    path: 'employers',
    component: EmployersComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Employers'
    },
    canActivate: [UserRouteAccessService],
    children: [...companyRoute, ...employerRoute]
  }
];
