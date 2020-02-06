import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IGeneratedLegalDocument } from 'app/shared/model/generated-legal-document.model';

type EntityResponseType = HttpResponse<IGeneratedLegalDocument>;
type EntityArrayResponseType = HttpResponse<IGeneratedLegalDocument[]>;

@Injectable({ providedIn: 'root' })
export class GeneratedLegalDocumentService {
  public resourceUrl = SERVER_API_URL + 'api/generated-legal-documents';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/generated-legal-documents';

  constructor(protected http: HttpClient) {}

  create(generatedLegalDocument: IGeneratedLegalDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(generatedLegalDocument);
    return this.http
      .post<IGeneratedLegalDocument>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(generatedLegalDocument: IGeneratedLegalDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(generatedLegalDocument);
    return this.http
      .put<IGeneratedLegalDocument>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IGeneratedLegalDocument>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGeneratedLegalDocument[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGeneratedLegalDocument[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(generatedLegalDocument: IGeneratedLegalDocument): IGeneratedLegalDocument {
    const copy: IGeneratedLegalDocument = Object.assign({}, generatedLegalDocument, {
      date: generatedLegalDocument.date != null && generatedLegalDocument.date.isValid() ? generatedLegalDocument.date.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date != null ? moment(res.body.date) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((generatedLegalDocument: IGeneratedLegalDocument) => {
        generatedLegalDocument.date = generatedLegalDocument.date != null ? moment(generatedLegalDocument.date) : null;
      });
    }
    return res;
  }
}
