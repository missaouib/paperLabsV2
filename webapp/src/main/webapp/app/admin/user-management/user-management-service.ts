import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { IUserInfoStatistic } from 'app/entities/UserInfoStatistic';
import { IStatistics } from 'app/entities/Statistics';

type usersStatisticsResponseType = HttpResponse<IStatistics>;
type userStatisticsResponseType = HttpResponse<IUserInfoStatistic>;

@Injectable({ providedIn: 'root' })
export class UserManagementService {
  private userManagementResource = SERVER_API_URL + 'api/statistic/user';

  constructor(private http: HttpClient) {}

  public getUsersStatistics(): Observable<usersStatisticsResponseType> {
    return this.http.get<IStatistics>(`${this.userManagementResource}`, { observe: 'response' });
  }

  public getUserStatistics(userId: number): Observable<userStatisticsResponseType> {
    return this.http.get<IUserInfoStatistic>(`${this.userManagementResource}/${userId}`, { observe: 'response' });
  }
}
