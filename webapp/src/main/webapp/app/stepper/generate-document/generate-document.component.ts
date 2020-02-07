import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { StepperApiService } from 'app/stepper/stepper/stepper-api.service';
import { AccountService } from 'app/core/auth/account.service';
import { FormBuilder, Validators } from '@angular/forms';
import { LoginService } from 'app/core/login/login.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { JhiEventManager, JhiLanguageService } from 'ng-jhipster';
import { Register } from 'app/account/register/register.service';
import { HttpErrorResponse } from '@angular/common/http';
import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/shared/constants/error.constants';
import { Account } from 'app/core/user/account.model';
import { StepperEventManagerService } from 'app/stepper/stepper/stepper-event-manager.service';
import { Subscription } from 'rxjs';
import { OrderService } from 'app/entities/order/order.service';
import { Order } from 'app/shared/model/order.model';

@Component({
  selector: 'jhi-generate-document',
  templateUrl: './generate-document.component.html',
  styleUrls: ['./generate-document.component.scss', '../stepper/stepper.scss', '../step/step.scss']
})
export class GenerateDocumentComponent implements OnInit, OnDestroy {
  checked = false;
  stepperData = {};
  saveDataAuthorization: string;
  legalDocumentId: string;
  userLogin: string;
  orderId: string;

  @Output() submitEvent = new EventEmitter();
  generateDocumentSubscriber: Subscription;

  // Login fields
  authenticationError: boolean;
  loginForm = this.formBuilder.group({
    username: [''],
    password: [''],
    rememberMe: [false]
  });

  // Register fields
  doNotMatch: string;
  error: string;
  errorEmailExists: string;
  errorUserExists: string;
  success: boolean;
  registerForm = this.formBuilder.group({
    firstName: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), Validators.pattern('^[ _.@A-Za-z0-9-]*$')]],
    lastName: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), Validators.pattern('^[ _.@A-Za-z0-9-]*$')]],
    email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]]
  });

  constructor(
    private route: ActivatedRoute,
    private stepperApiService: StepperApiService,
    private router: Router,
    private accountService: AccountService,
    private formBuilder: FormBuilder,
    private loginService: LoginService,
    private stateStorageService: StateStorageService,
    private eventManager: JhiEventManager,
    private registerService: Register,
    private languageService: JhiLanguageService,
    private stepperEventManagerService: StepperEventManagerService,
    private orderService: OrderService
  ) {}

  ngOnInit() {
    this.generateDocumentSubscriber = this.stepperEventManagerService.getGenerateDocument().subscribe(res => {
      this.legalDocumentId = res.legalDocumentId;
      this.stepperData = res.stepperData;
      this.orderId = res.orderId;
      this.submit();
    });
    this.success = false;
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  loginAndDownload() {
    this.loginService
      .login({
        username: this.loginForm.get('username').value,
        password: this.loginForm.get('password').value,
        rememberMe: this.loginForm.get('rememberMe').value
      })
      .then(() => {
        this.authenticationError = false;

        this.eventManager.broadcast({
          name: 'authenticationSuccess',
          content: 'Sending Authentication Success'
        });
        this.userLogin = this.loginForm.get('username').value;
        // previousState was set in the authExpiredInterceptor before being redirected to login modal.
        // since login is successful, go to stored previousState and clear previousState
        const redirect = this.stateStorageService.getUrl();
        if (redirect) {
          this.stateStorageService.storeUrl(null);
          this.router.navigateByUrl(redirect);
        }

        // Submit legal document generation
        this.submitEvent.emit();
      })
      .catch(() => {
        this.authenticationError = true;
      });
  }

  requestResetPassword() {
    this.router.navigate([]).then(res => {
      window.open('/account/reset/request', '_blank');
    });
  }

  registerAndConfirmOrder() {
    let registerAccount = {};
    const firstName = this.registerForm.get(['firstName']).value;
    const lastName = this.registerForm.get(['lastName']).value;
    const email = this.registerForm.get(['email']).value;
    const login = this.registerForm.get(['email']).value;
    const password = this.registerForm.get(['password']).value;
    if (password !== this.registerForm.get(['confirmPassword']).value) {
      this.doNotMatch = 'ERROR';
    } else {
      registerAccount = { ...registerAccount, lastName, firstName, email, login, password };
      this.doNotMatch = null;
      this.error = null;
      this.errorUserExists = null;
      this.errorEmailExists = null;
      this.languageService.getCurrent().then(langKey => {
        registerAccount = { ...registerAccount, langKey };
        const order: Order = new Order();
        this.orderService.create(order).subscribe(res => {
          const orderId: string = res.body.id;
          this.registerService.save(registerAccount, orderId).subscribe(
            () => {
              this.success = true;
              this.userLogin = login;
              // Submit legal document generation
              this.submitEvent.emit(orderId);
              this.router.navigate(['/account/register', { withOrder: true }]);
            },
            response => this.processError(response)
          );
        });
      });
    }
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists = 'ERROR';
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = 'ERROR';
    } else {
      this.error = 'ERROR';
    }
  }

  private submit() {
   // alert("eeeeeeeeeeeeeeeeeeeeeee");
    this.accountService.identity().then((account: Account) => {
      if (account !== null) {
        this.userLogin = account.login;
      }
      if (this.userLogin === null || this.userLogin === undefined) {
        throw new Error('userLogin is : ' + this.userLogin);
      } else {
        this.saveDataAuthorization = String(this.checked);
        this.stepperApiService
          .generateDocXFile(this.stepperData, this.legalDocumentId, this.saveDataAuthorization, this.userLogin, this.orderId)
          .subscribe(res => {
            this.stepperEventManagerService.sendOrder(res.body);
            
             let str =res.body.generatedLegalDocument.generatedWordFilePath;

           //  str=str.slice(22);
             str=str.slice(54);
            this.download(str);
          });
      }
    });
  }

  download(url){  
     location.href =url;

  }

  ngOnDestroy(): void {
    this.generateDocumentSubscriber.unsubscribe();
  }
}
