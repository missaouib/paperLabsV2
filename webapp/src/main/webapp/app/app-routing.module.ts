import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute } from './layouts/error/error.route';
import { navbarRoute } from './layouts/navbar/navbar.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { mainMenuRoute } from 'app/layouts/main-menu/main-menu.route';

const LAYOUT_ROUTES = [navbarRoute, mainMenuRoute, ...errorRoute];

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: 'admin',
          loadChildren: () => import('./admin/admin.module').then(m => m.PaperlabsAdminModule)
        },
        {
          path: 'account',
          loadChildren: () => import('./account/account.module').then(m => m.PaperlabsAccountModule)
        },
        {
          path: 'stepper',
          loadChildren: () => import('./stepper/stepper.module').then(m => m.StepperModule)
        },
        {
          path: 'user',
          loadChildren: () => import('./user/user.module').then(m => m.UserModule)
        },
        ...LAYOUT_ROUTES
      ],
      { enableTracing: DEBUG_INFO_ENABLED }
    )
  ],
  exports: [RouterModule]
})
export class PaperlabsAppRoutingModule {}
