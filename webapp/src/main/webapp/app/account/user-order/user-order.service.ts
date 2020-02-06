import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IOrder } from 'app/shared/model/order.model';

type EntityResponseType = HttpResponse<IOrder>;
type EntityArrayResponseType = HttpResponse<IOrder[]>;

@Injectable({ providedIn: 'root' })
export class UserOrderService {
  public resourceUrl = SERVER_API_URL + 'api/orders';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/orders';
  public resourceByUserLoginUrl = SERVER_API_URL + 'api/orders/user';
  public resourceByAuthenticatedUserUrl = SERVER_API_URL + 'api/orders/authenticatedUser';

  constructor(protected http: HttpClient) {}

  create(order: IOrder): Observable<EntityResponseType> {
    return this.http.post<IOrder>(this.resourceUrl, order, { observe: 'response' });
  }

  update(order: IOrder): Observable<EntityResponseType> {
    return this.http.put<IOrder>(this.resourceUrl, order, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrder[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrder[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getAllOrdersByUserLogin(userId: string, pageNumber: number, itemsPerPage: number): Observable<EntityArrayResponseType> {
    return this.http.get<IOrder[]>(`${this.resourceByUserLoginUrl}/${userId}?pageNumber=${pageNumber}&itemsPerPage=${itemsPerPage}`, {
      observe: 'response'
    });
  }

  findByAuthenticatedUser(orderId: string): Observable<EntityResponseType> {
    return this.http.get<IOrder>(`${this.resourceByAuthenticatedUserUrl}/${orderId}`, { observe: 'response' });
  }

  transformOrderIdentifier(orderIdentifier: string): string {
    return Number(orderIdentifier).toLocaleString('en-US', { minimumIntegerDigits: 5, useGrouping: false });
  }
}
