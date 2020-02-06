import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILegalDocument } from 'app/shared/model/legal-document.model';
import { IDocumentInfoStatistic } from 'app/entities/DocumentInfoStatistic';

type EntityResponseType = HttpResponse<ILegalDocument>;
type EntityArrayResponseType = HttpResponse<ILegalDocument[]>;
type EntityLegalDocumentResponseType = HttpResponse<ILegalDocument[]>;
type EntityLegalDocumentInfoStatistic = HttpResponse<IDocumentInfoStatistic>;

@Injectable({ providedIn: 'root' })
export class LegalDocumentService {
  public resourceUrl = SERVER_API_URL + 'api/legal-documents';
  public resourcePopularUrl = SERVER_API_URL + 'api/popular-legal-documents';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/legal-documents';
  public resourceSearchUrlByFields = SERVER_API_URL + 'api/_search/legal-documents/fields';
  public resourceStatistics = SERVER_API_URL + 'api/statistic/document';

  constructor(protected http: HttpClient) {}

  create(legalDocument: ILegalDocument): Observable<EntityResponseType> {
    return this.http.post<ILegalDocument>(this.resourceUrl, legalDocument, { observe: 'response' });
  }

  update(legalDocument: ILegalDocument): Observable<EntityResponseType> {
    return this.http.put<ILegalDocument>(this.resourceUrl, legalDocument, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ILegalDocument>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILegalDocument[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILegalDocument[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  searchLegalDocumentsWithFields(query?: any): Observable<EntityLegalDocumentResponseType> {
    query = query.trim();
    const options = new HttpParams().set('query', query);
    return this.http.get<ILegalDocument[]>(this.resourceSearchUrlByFields, { params: options, observe: 'response' });
  }

  getDocumentStatisticsInfo(legalDocumentId: string): Observable<EntityLegalDocumentInfoStatistic> {
    return this.http.get<IDocumentInfoStatistic>(`${this.resourceStatistics}/${legalDocumentId}`, { observe: 'response' });
  }

  getAllLegalDocuments(): Observable<EntityArrayResponseType> {
    return this.http.get<ILegalDocument[]>(`${this.resourceUrl}`, { observe: 'response' });
  }

  getPopularLegalDocuments(): Observable<EntityArrayResponseType> {
    return this.http.get<ILegalDocument[]>(`${this.resourcePopularUrl}`, { observe: 'response' });
  }
}
