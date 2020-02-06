import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';

@Injectable({ providedIn: 'root' })
export class Register {
  constructor(private http: HttpClient) {}

  save(account: any, orderId?: string): Observable<any> {
    const options = createRequestOption({ orderId });
    return this.http.post(SERVER_API_URL + 'api/register', account, { params: options, observe: 'response' });
  }
}
