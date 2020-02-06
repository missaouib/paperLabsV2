import { Routes } from '@angular/router';
import { profileRoute } from 'app/account/profile/profile.route';
import { registerRoute } from 'app/account/register/register.route';
import { activateRoute } from 'app/account/activate/activate.route';

export const accountState: Routes = [
  {
    path: '',
    children: [...profileRoute, registerRoute, activateRoute]
  }
];
