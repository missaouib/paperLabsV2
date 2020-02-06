import { Injectable, Injector } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { JhiTrackerService } from 'app/core/tracker/tracker.service';

@Injectable({ providedIn: 'root' })
export class LoginService {
  constructor(
    private accountService: AccountService,
    private trackerService: JhiTrackerService,
    private authServerProvider: AuthServerProvider,
    private injector: Injector
  ) {}

  login(credentials, callback?) {
    const cb = callback || function() {};

    return new Promise((resolve, reject) => {
      this.authServerProvider.login(credentials).subscribe(
        data => {
          if (!this.accountService) {
            this.accountService = this.injector.get(AccountService);
          }
          this.accountService.identity(true).then(account => {
            this.trackerService.sendActivity();
            resolve(data);
          });
          return cb();
        },
        err => {
          this.logout();
          reject(err);
          return cb(err);
        }
      );
    });
  }

  loginWithToken(jwt, rememberMe) {
    return this.authServerProvider.loginWithToken(jwt, rememberMe);
  }

  logout() {
    if (!this.accountService) {
      this.accountService = this.injector.get(AccountService);
    }
    this.authServerProvider.logout().subscribe(null, null, () => this.accountService.authenticate(null));
  }
}
