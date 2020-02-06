import { NgModule } from '@angular/core';
import { PaperlabsSharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { JhiAlertComponent } from './alert/alert.component';
import { JhiAlertErrorComponent } from './alert/alert-error.component';
import { JhiLoginModalComponent } from './login/login.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { PasswordStrengthBarComponent } from 'app/account/password/password-strength-bar.component';

@NgModule({
  imports: [PaperlabsSharedLibsModule],
  declarations: [
    FindLanguageFromKeyPipe,
    JhiAlertComponent,
    JhiAlertErrorComponent,
    JhiLoginModalComponent,
    HasAnyAuthorityDirective,
    PasswordStrengthBarComponent
  ],
  entryComponents: [JhiLoginModalComponent],
  exports: [
    PaperlabsSharedLibsModule,
    FindLanguageFromKeyPipe,
    JhiAlertComponent,
    JhiAlertErrorComponent,
    JhiLoginModalComponent,
    HasAnyAuthorityDirective,
    PasswordStrengthBarComponent
  ]
})
export class PaperlabsSharedModule {}
