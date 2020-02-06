import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';

import { IContact } from 'app/entities/Contact';

@Injectable({ providedIn: 'root' })
export class ContactService {
  public contactResourceUrl = SERVER_API_URL + 'api/contact';
  public contactExpertResourceUrl = SERVER_API_URL + 'api/contactExpert';

  constructor(protected http: HttpClient) {}

  sendContactEmail(contact: IContact): Observable<HttpResponse<IContact>> {
    return this.http.post<IContact>(`${this.contactResourceUrl}`, contact, { observe: 'response' });
  }

  sendContactExpertEmail(contact: IContact): Observable<HttpResponse<IContact>> {
    return this.http.post<IContact>(`${this.contactExpertResourceUrl}`, contact, { observe: 'response' });
  }
}
