import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILawyer } from 'app/shared/model/lawyer.model';

type EntityResponseType = HttpResponse<ILawyer>;
type EntityArrayResponseType = HttpResponse<ILawyer[]>;

@Injectable({ providedIn: 'root' })
export class LawyerService {
  public resourceUrl = SERVER_API_URL + 'api/lawyers';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/lawyers';

  constructor(protected http: HttpClient) {}

  create(lawyer: ILawyer): Observable<EntityResponseType> {
    return this.http.post<ILawyer>(this.resourceUrl, lawyer, { observe: 'response' });
  }

  update(lawyer: ILawyer): Observable<EntityResponseType> {
    return this.http.put<ILawyer>(this.resourceUrl, lawyer, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ILawyer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILawyer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILawyer[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
