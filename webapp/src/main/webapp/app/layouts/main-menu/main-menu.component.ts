import { Component, OnInit, OnDestroy, AfterViewInit, ViewChild, ElementRef, HostListener } from '@angular/core';
import { LoginService } from 'app/core/login/login.service';
import { JhiAlertService, JhiEventManager, JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core/language/language.helper';
import { SessionStorageService } from 'ngx-webstorage';
import { AccountService } from 'app/core/auth/account.service';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { Account } from 'app/core/user/account.model';
import { LegalDocumentService } from 'app/entities/legal-document/legal-document.service';
import { ILegalDocument } from 'app/shared/model/legal-document.model';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-main-menu',
  templateUrl: './main-menu.component.html',
  styleUrls: ['./main-menu.component.scss']
})
export class MainMenuComponent implements OnInit, OnDestroy, AfterViewInit {
  modalRef: NgbModalRef;
  authSubscription: Subscription;
  account: Account;
  isNavbarCollapsed: boolean;
  legalDocuments: ILegalDocument[] = [];
  popularLegalDocuments: ILegalDocument[] = [];
  title = 'landing-page';
  sticky = false;
  windowScroll: any;
  @ViewChild('stickyMenu', { static: false }) menuElement: ElementRef;
  elementPosition: any;
  section = 'home-section';
  numberOfLegalDocumentsToShow = 10;

  constructor(
    private loginService: LoginService,
    private languageService: JhiLanguageService,
    private languageHelper: JhiLanguageHelper,
    private sessionStorage: SessionStorageService,
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private profileService: ProfileService,
    public router: Router,
    private eventManager: JhiEventManager,
    private jhiAlertService: JhiAlertService,
    private legalDocumentService: LegalDocumentService
  ) {
    this.isNavbarCollapsed = true;
  }

  ngOnInit(): void {
    this.accountService.identity().then((account: Account) => {
      this.account = account;
    });
    this.registerAuthenticationSuccess();

    this.legalDocumentService
      .query()
      .subscribe(
        (res: HttpResponse<ILegalDocument[]>) => (this.legalDocuments = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );

    this.legalDocumentService
      .getPopularLegalDocuments()
      .subscribe(
        (res: HttpResponse<ILegalDocument[]>) => (this.popularLegalDocuments = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  registerAuthenticationSuccess() {
    this.authSubscription = this.eventManager.subscribe('authenticationSuccess', message => {
      this.accountService.identity().then(account => {
        this.account = account;
      });
    });
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  login() {
    this.collapseNavbar();
    this.modalRef = this.loginModalService.open();
  }

  logout() {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar() {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  collapseNavbar() {
    this.isNavbarCollapsed = true;
  }

  getImageUrl() {
    return this.isAuthenticated() ? this.accountService.getImageUrl() : null;
  }

  getAccountName() {
    if (this.isAuthenticated()) {
      return this.account.firstName === null || this.account.lastName === null
        ? this.account.login
        : this.account.firstName + ' ' + this.account.lastName;
    } else return null;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  ngOnDestroy() {
    if (this.authSubscription) {
      this.eventManager.destroy(this.authSubscription);
    }
  }

  hasRoleAdmin(): boolean {
    return this.accountService.hasAnyAuthority(['ROLE_ADMIN']);
  }

  filterByKeyword(keyword): ILegalDocument[] {
    return this.legalDocuments.filter(x => x.keywords.includes(keyword));
  }

  ngAfterViewInit(): void {
    if (this.menuElement !== undefined && this.menuElement.nativeElement !== undefined) {
      this.elementPosition = this.menuElement.nativeElement.offsetTop;
    }
  }

  @HostListener('window:scroll')
  handleScroll() {
    this.windowScroll = window.pageYOffset;
    this.windowScroll >= this.elementPosition ? (this.sticky = true) : (this.sticky = false);
  }
}
