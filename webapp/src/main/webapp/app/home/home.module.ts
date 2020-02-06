import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PaperlabsSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { StepperModule } from 'app/stepper/stepper.module';
import { HomePageSection1Component } from 'app/home/home-page-components/home-page-section-1-component/home-page-section-1.component';
import { HomePageSection2Component } from 'app/home/home-page-components/home-page-section-2-component/home-page-section-2.component';
import { HomePageSection3Component } from 'app/home/home-page-components/home-page-section-3-component/home-page-section-3.component';
import { HomePageSection4Component } from 'app/home/home-page-components/home-page-section-4-component/home-page-section-4.component';

@NgModule({
  declarations: [HomeComponent, HomePageSection1Component, HomePageSection2Component, HomePageSection3Component, HomePageSection4Component],
  imports: [PaperlabsSharedModule, RouterModule.forChild([HOME_ROUTE]), StepperModule]
})
export class PaperlabsHomeModule {}
