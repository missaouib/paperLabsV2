import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDowloadHistory } from 'app/shared/model/dowload-history.model';

type EntityResponseType = HttpResponse<IDowloadHistory>;
type EntityArrayResponseType = HttpResponse<IDowloadHistory[]>;

@Injectable({ providedIn: 'root' })
export class DowloadHistoryService {
  public resourceUrl = SERVER_API_URL + 'api/dowload-histories';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/dowload-histories';

  constructor(protected http: HttpClient) {}

  create(dowloadHistory: IDowloadHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dowloadHistory);
    return this.http
      .post<IDowloadHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dowloadHistory: IDowloadHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dowloadHistory);
    return this.http
      .put<IDowloadHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IDowloadHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDowloadHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDowloadHistory[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(dowloadHistory: IDowloadHistory): IDowloadHistory {
    const copy: IDowloadHistory = Object.assign({}, dowloadHistory, {
      date: dowloadHistory.date != null && dowloadHistory.date.isValid() ? dowloadHistory.date.toJSON() : null
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
      res.body.forEach((dowloadHistory: IDowloadHistory) => {
        dowloadHistory.date = dowloadHistory.date != null ? moment(dowloadHistory.date) : null;
      });
    }
    return res;
  }
}
