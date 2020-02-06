import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Lawyer } from 'app/shared/model/lawyer.model';
import { LawyerService } from './lawyer.service';
import { LawyerComponent } from './lawyer.component';
import { LawyerDetailComponent } from './lawyer-detail.component';
import { LawyerUpdateComponent } from './lawyer-update.component';
import { LawyerDeletePopupComponent } from './lawyer-delete-dialog.component';
import { ILawyer } from 'app/shared/model/lawyer.model';

@Injectable({ providedIn: 'root' })
export class LawyerResolve implements Resolve<ILawyer> {
  constructor(private service: LawyerService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ILawyer> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Lawyer>) => response.ok),
        map((lawyer: HttpResponse<Lawyer>) => lawyer.body)
      );
    }
    return of(new Lawyer());
  }
}

export const lawyerRoute: Routes = [
  {
    path: '',
    component: LawyerComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'paperlabsApp.lawyer.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: LawyerDetailComponent,
    resolve: {
      lawyer: LawyerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.lawyer.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: LawyerUpdateComponent,
    resolve: {
      lawyer: LawyerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.lawyer.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: LawyerUpdateComponent,
    resolve: {
      lawyer: LawyerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.lawyer.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const lawyerPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: LawyerDeletePopupComponent,
    resolve: {
      lawyer: LawyerResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.lawyer.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
