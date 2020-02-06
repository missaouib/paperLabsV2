import { NgModule } from '@angular/core';
import { InputComponent } from './input/input.component';
import { BlocComponent } from './bloc/bloc.component';
import { StepComponent } from './step/step.component';
import { PaperlabsSharedModule } from 'app/shared/shared.module';
import { StepperComponent } from './stepper/stepper.component';
import { RouterModule } from '@angular/router';
import { stepperRoutes } from 'app/stepper/stepper.route';
import { GenerateDocumentComponent } from './generate-document/generate-document.component';
import { PaymentStepComponent } from './payment-step/payment-step.component';
import { JhiAutoFillModalComponent } from './auto-fill-modal/auto-fill-modal.component';
import { PreviewComponent } from './preview/preview.component';
import { PreviewModalComponent } from './preview/preview-modal/preview-modal.component';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';


@NgModule({
  declarations: [
    InputComponent,
    BlocComponent,
    StepComponent,
    StepperComponent,
    GenerateDocumentComponent,
    PaymentStepComponent,
    JhiAutoFillModalComponent,
    PreviewComponent,
    PreviewModalComponent
  ],
  exports: [InputComponent, BlocComponent, StepComponent, StepperComponent],
  imports: [PaperlabsSharedModule, RouterModule.forChild(stepperRoutes), BsDatepickerModule.forRoot()],
  entryComponents: [JhiAutoFillModalComponent, PreviewModalComponent]
})
export class StepperModule {}
