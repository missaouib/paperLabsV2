import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { GeneratedLegalDocument } from 'app/shared/model/generated-legal-document.model';
import { GeneratedLegalDocumentService } from './generated-legal-document.service';
import { GeneratedLegalDocumentComponent } from './generated-legal-document.component';
import { GeneratedLegalDocumentDetailComponent } from './generated-legal-document-detail.component';
import { GeneratedLegalDocumentUpdateComponent } from './generated-legal-document-update.component';
import { GeneratedLegalDocumentDeletePopupComponent } from './generated-legal-document-delete-dialog.component';
import { IGeneratedLegalDocument } from 'app/shared/model/generated-legal-document.model';

@Injectable({ providedIn: 'root' })
export class GeneratedLegalDocumentResolve implements Resolve<IGeneratedLegalDocument> {
  constructor(private service: GeneratedLegalDocumentService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IGeneratedLegalDocument> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<GeneratedLegalDocument>) => response.ok),
        map((generatedLegalDocument: HttpResponse<GeneratedLegalDocument>) => generatedLegalDocument.body)
      );
    }
    return of(new GeneratedLegalDocument());
  }
}

export const generatedLegalDocumentRoute: Routes = [
  {
    path: '',
    component: GeneratedLegalDocumentComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'paperlabsApp.generatedLegalDocument.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: GeneratedLegalDocumentDetailComponent,
    resolve: {
      generatedLegalDocument: GeneratedLegalDocumentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.generatedLegalDocument.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: GeneratedLegalDocumentUpdateComponent,
    resolve: {
      generatedLegalDocument: GeneratedLegalDocumentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.generatedLegalDocument.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: GeneratedLegalDocumentUpdateComponent,
    resolve: {
      generatedLegalDocument: GeneratedLegalDocumentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.generatedLegalDocument.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const generatedLegalDocumentPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: GeneratedLegalDocumentDeletePopupComponent,
    resolve: {
      generatedLegalDocument: GeneratedLegalDocumentResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'paperlabsApp.generatedLegalDocument.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
