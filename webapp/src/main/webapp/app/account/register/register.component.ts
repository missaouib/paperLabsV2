import { Component, OnInit, AfterViewInit, Renderer, ElementRef } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiLanguageService } from 'ng-jhipster';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/shared/constants/error.constants';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { Register } from './register.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-register',
  templateUrl: './register.component.html'
})
export class RegisterComponent implements OnInit, AfterViewInit {
  doNotMatch: string;
  error: string;
  errorEmailExists: string;
  errorUserExists: string;
  success: boolean;
  modalRef: NgbModalRef;
  isRedirectedFromStepper: boolean;

  registerForm = this.fb.group({
    firstName: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), Validators.pattern('^[ _.@A-Za-z0-9-]*$')]],
    lastName: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), Validators.pattern('^[ _.@A-Za-z0-9-]*$')]],
    email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]]
  });

  constructor(
    private languageService: JhiLanguageService,
    private loginModalService: LoginModalService,
    private registerService: Register,
    private elementRef: ElementRef,
    private renderer: Renderer,
    private fb: FormBuilder,
    private route: ActivatedRoute
  ) {
    this.isRedirectedFromStepper = false;
  }

  ngOnInit() {
    this.isRedirectedFromStepper = this.route.snapshot.paramMap.get('withOrder') === 'true';
    this.success = this.isRedirectedFromStepper;
  }

  ngAfterViewInit() {
    if (this.success === false) {
      this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#firstName'), 'focus', []);
    }
  }

  register() {
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
        // eslint-disable-next-line no-console
        console.log(registerAccount);
        this.registerService.save(registerAccount).subscribe(
          () => {
            this.success = true;
          },
          response => this.processError(response)
        );
      });
    }
  }

  openLogin() {
    this.modalRef = this.loginModalService.open();
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
}
