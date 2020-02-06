import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DescriptionSection } from 'app/shared/model/description-section.model';
import { DescriptionSectionService } from './description-section.service';
import { DescriptionSectionComponent } from './description-section.component';
import { DescriptionSectionDetailComponent } from './description-section-detail.component';
import { DescriptionSectionUpdateComponent } from './description-section-update.component';
import { DescriptionSectionDeletePopupComponent } from './description-section-delete-dialog.component';
import { IDescriptionSection } from 'app/shared/model/description-section.model';

@Injectable({ providedIn: 'root' })
export class DescriptionSectionResolve implements Resolve<IDescriptionSection> {
  constructor(private service: DescriptionSectionService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDescriptionSection> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<DescriptionSection>) => response.ok),
        map((descriptionSection: HttpResponse<DescriptionSection>) => descriptionSection.body)
      );
    }
    return of(new DescriptionSection());
  }
}

export const descriptionSectionRoute: Routes = [
  {
    path: '',
    component: DescriptionSectionComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'paperlabsApp.descriptionSection.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DescriptionSectionDetailComponent,
    resolve: {
      descriptionSection: DescriptionSectionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.descriptionSection.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DescriptionSectionUpdateComponent,
    resolve: {
      descriptionSection: DescriptionSectionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.descriptionSection.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DescriptionSectionUpdateComponent,
    resolve: {
      descriptionSection: DescriptionSectionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.descriptionSection.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const descriptionSectionPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DescriptionSectionDeletePopupComponent,
    resolve: {
      descriptionSection: DescriptionSectionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.descriptionSection.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
