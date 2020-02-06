import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DowloadHistory } from 'app/shared/model/dowload-history.model';
import { DowloadHistoryService } from './dowload-history.service';
import { DowloadHistoryComponent } from './dowload-history.component';
import { DowloadHistoryDetailComponent } from './dowload-history-detail.component';
import { DowloadHistoryUpdateComponent } from './dowload-history-update.component';
import { DowloadHistoryDeletePopupComponent } from './dowload-history-delete-dialog.component';
import { IDowloadHistory } from 'app/shared/model/dowload-history.model';

@Injectable({ providedIn: 'root' })
export class DowloadHistoryResolve implements Resolve<IDowloadHistory> {
  constructor(private service: DowloadHistoryService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDowloadHistory> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<DowloadHistory>) => response.ok),
        map((dowloadHistory: HttpResponse<DowloadHistory>) => dowloadHistory.body)
      );
    }
    return of(new DowloadHistory());
  }
}

export const dowloadHistoryRoute: Routes = [
  {
    path: '',
    component: DowloadHistoryComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'paperlabsApp.dowloadHistory.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DowloadHistoryDetailComponent,
    resolve: {
      dowloadHistory: DowloadHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.dowloadHistory.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DowloadHistoryUpdateComponent,
    resolve: {
      dowloadHistory: DowloadHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.dowloadHistory.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DowloadHistoryUpdateComponent,
    resolve: {
      dowloadHistory: DowloadHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.dowloadHistory.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const dowloadHistoryPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DowloadHistoryDeletePopupComponent,
    resolve: {
      dowloadHistory: DowloadHistoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.dowloadHistory.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
