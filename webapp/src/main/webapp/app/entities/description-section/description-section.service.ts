import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDescriptionSection } from 'app/shared/model/description-section.model';

type EntityResponseType = HttpResponse<IDescriptionSection>;
type EntityArrayResponseType = HttpResponse<IDescriptionSection[]>;

@Injectable({ providedIn: 'root' })
export class DescriptionSectionService {
  public resourceUrl = SERVER_API_URL + 'api/description-sections';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/description-sections';

  constructor(protected http: HttpClient) {}

  create(descriptionSection: IDescriptionSection): Observable<EntityResponseType> {
    return this.http.post<IDescriptionSection>(this.resourceUrl, descriptionSection, { observe: 'response' });
  }

  update(descriptionSection: IDescriptionSection): Observable<EntityResponseType> {
    return this.http.put<IDescriptionSection>(this.resourceUrl, descriptionSection, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IDescriptionSection>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDescriptionSection[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDescriptionSection[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
