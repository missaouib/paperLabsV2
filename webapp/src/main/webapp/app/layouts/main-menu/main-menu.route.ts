import { Route } from '@angular/router';
import { MainMenuComponent } from 'app/layouts/main-menu/main-menu.component';

export const mainMenuRoute: Route = {
  path: '',
  component: MainMenuComponent,
  outlet: 'main-menu'
};
