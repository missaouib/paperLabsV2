import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { LegalDocument } from 'app/shared/model/legal-document.model';
import { LegalDocumentService } from './legal-document.service';
import { LegalDocumentComponent } from './legal-document.component';
import { LegalDocumentDetailComponent } from './legal-document-detail.component';
import { LegalDocumentUpdateComponent } from './legal-document-update.component';
import { LegalDocumentDeletePopupComponent } from './legal-document-delete-dialog.component';
import { ILegalDocument } from 'app/shared/model/legal-document.model';

@Injectable({ providedIn: 'root' })
export class LegalDocumentResolve implements Resolve<ILegalDocument> {
  constructor(private service: LegalDocumentService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ILegalDocument> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<LegalDocument>) => response.ok),
        map((legalDocument: HttpResponse<LegalDocument>) => legalDocument.body)
      );
    }
    return of(new LegalDocument());
  }
}

export const legalDocumentRoute: Routes = [
  {
    path: '',
    component: LegalDocumentComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'paperlabsApp.legalDocument.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: LegalDocumentDetailComponent,
    resolve: {
      legalDocument: LegalDocumentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.legalDocument.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: LegalDocumentUpdateComponent,
    resolve: {
      legalDocument: LegalDocumentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.legalDocument.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: LegalDocumentUpdateComponent,
    resolve: {
      legalDocument: LegalDocumentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.legalDocument.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const legalDocumentPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: LegalDocumentDeletePopupComponent,
    resolve: {
      legalDocument: LegalDocumentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.legalDocument.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
