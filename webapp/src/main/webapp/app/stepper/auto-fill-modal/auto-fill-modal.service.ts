import { Injectable } from '@angular/core';
import { ILegalDocument } from 'app/shared/model/legal-document.model';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { Company } from 'app/shared/model/company.model';
import { Employer } from 'app/shared/model/employer.model';

type userCompaniesResponseType = HttpResponse<Company[]>;
type userEmployersResponseType = HttpResponse<Employer[]>;

@Injectable({ providedIn: 'root' })
export class AutoFillModalService {
  private legalDocument: ILegalDocument;
  private legalDocumentSource = new BehaviorSubject(this.legalDocument);
  currentLegalDocument = this.legalDocumentSource.asObservable();
  private userLoginSource = new BehaviorSubject('');
  currentUserLogin = this.userLoginSource.asObservable();

  public userCompaniesResourceUrl = SERVER_API_URL + 'api/companies/user';
  public userEmployersResourceUrl = SERVER_API_URL + 'api/employers/user';
  public currentUserHasConcernedEntityObjectsUrl = SERVER_API_URL + 'api/currentUserHasConcernedEntityObjects';

  constructor(protected http: HttpClient) {}

  setLegalDocument(legalDocument: ILegalDocument) {
    this.legalDocumentSource.next(legalDocument);
  }

  setUserLogin(userLogin: string) {
    this.userLoginSource.next(userLogin);
  }

  searchCompaniesByUser(userId: string): Observable<userCompaniesResponseType> {
    return this.http.get<Company[]>(`${this.userCompaniesResourceUrl}/${userId}`, { observe: 'response' });
  }

  searchEmployersByUser(userId: string): Observable<userEmployersResponseType> {
    return this.http.get<Employer[]>(`${this.userEmployersResourceUrl}/${userId}`, { observe: 'response' });
  }

  currentUserHasConcernedEntityObjects(legalDocumentId: string, userLogin: string): Observable<HttpResponse<any>> {
    return this.http.get<any>(`${this.currentUserHasConcernedEntityObjectsUrl}?legalDocumentId=${legalDocumentId}&userLogin=${userLogin}`, {
      observe: 'response'
    });
  }
}
