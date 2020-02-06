import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { EmployerComponent } from 'app/account/employers/employer/employer.component';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Employer, IEmployer } from 'app/shared/model/employer.model';
import { EmployerService } from 'app/account/employers/employer/employer.service';
import { EmployerDetailComponent } from 'app/account/employers/employer/employer-detail.component';

@Injectable({ providedIn: 'root' })
export class EmployerResolve implements Resolve<IEmployer> {
  constructor(private service: EmployerService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IEmployer> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Employer>) => response.ok),
        map((employer: HttpResponse<Employer>) => employer.body)
      );
    }
    return of(new Employer());
  }
}
export const employerRoute: Routes = [
  {
    path: 'employer',
    component: EmployerComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'paperlabsApp.user-employers.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'employer/:id/view',
    component: EmployerDetailComponent,
    resolve: {
      employer: EmployerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.user-employers.details'
    },
    canActivate: [UserRouteAccessService]
  }
];
