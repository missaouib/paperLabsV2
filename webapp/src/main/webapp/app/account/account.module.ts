import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PaperlabsSharedModule } from 'app/shared/shared.module';

import { RegisterComponent } from './register/register.component';
import { ActivateComponent } from './activate/activate.component';
import { PasswordComponent } from './password/password.component';
import { PasswordResetInitComponent } from './password-reset/init/password-reset-init.component';
import { PasswordResetFinishComponent } from './password-reset/finish/password-reset-finish.component';
import { SettingsComponent } from './settings/settings.component';
import { accountState } from './account.route';
import { ProfileComponent } from './profile/profile.component';
import { UserOrderComponent } from 'app/account/user-order/user-order.component';
import { UserOrderDetailComponent } from 'app/account/user-order/user-order-detail.component';
import { EmployersModule } from 'app/account/employers/employers.module';
import { StepperModule } from 'app/stepper/stepper.module';

@NgModule({
  imports: [PaperlabsSharedModule, EmployersModule, StepperModule, RouterModule.forChild(accountState)],
  declarations: [
    ActivateComponent,
    RegisterComponent,
    PasswordComponent,
    PasswordResetInitComponent,
    PasswordResetFinishComponent,
    SettingsComponent,
    ProfileComponent,
    UserOrderComponent,
    UserOrderDetailComponent
  ]
})
export class PaperlabsAccountModule {}
