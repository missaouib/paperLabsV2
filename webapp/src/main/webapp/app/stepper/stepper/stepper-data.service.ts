import { Injectable } from '@angular/core';
import { StepEntity } from 'app/entities/StepEntity';
import { StepperDomManipulationService } from 'app/stepper/stepper/stepper-dom-manipulation.service';
import { StepperEventManagerService } from 'app/stepper/stepper/stepper-event-manager.service';
import { IOrder } from 'app/shared/model/order.model';
import { StepperApiService } from 'app/stepper/stepper/stepper-api.service';
import { StepperDataConvertingService } from 'app/stepper/stepper/stepper-data-converting.service';
import * as fileSaver from 'file-saver';

@Injectable({ providedIn: 'root' })
export class StepperDataService {
  constructor(
    private stepperEventManagerService: StepperEventManagerService,
    private stepperApiService: StepperApiService,
    private stepperDataConvertingService: StepperDataConvertingService
  ) {}

  private static getStepEntity(stepsEntity: StepEntity[], stepId: string): StepEntity {
    for (let i = 0; i < stepsEntity.length; i++) {
      if (stepsEntity[i].id === stepId) {
        return stepsEntity[i];
      }
    }
  }

  addCurrentStepInputsValue(stepsEntity: StepEntity[], workflowStepsInputs: Map<string, string>[], currentStepId: string) {
    const currentStepInputsValue: Map<string, string> = new Map<string, string>();
    StepperDataService.getStepEntity(stepsEntity, currentStepId).blocs.forEach(bloc => {
      if (StepperDomManipulationService.getHTMLInputElement(bloc.id).style.display === 'block') {
        bloc.inputs.forEach(input => {
          const htmlElement = StepperDomManipulationService.getHTMLInputElement(input.id);
          if (StepperDomManipulationService.getHTMLInputElement('InputDiv' + input.id).style.display === 'block') {
            if (htmlElement.type === 'radio') {
              if (htmlElement.checked) {
                currentStepInputsValue.set(input.id, htmlElement.value);
              }
            } else if (htmlElement.type === 'checkbox') {
              currentStepInputsValue.set(input.id, String(htmlElement.checked));
            } else {
              if (htmlElement.value !== '') {
                currentStepInputsValue.set(input.id, htmlElement.value);
              }
            }
          }
        });
      }
    });
    workflowStepsInputs.push(currentStepInputsValue);
  }

  generateDocument(stepsEntity: StepEntity[], workflowStepsInputs: Map<string, string>[], legalDocumentId: string, orderId: string) {
    const stepperData = this.stepperDataConvertingService.convert(stepsEntity, workflowStepsInputs, legalDocumentId);
    this.stepperEventManagerService.sendGenerateDocument(stepperData, legalDocumentId, orderId);
  }

  downloadPDF(order: IOrder) {
    const fileName = order.generatedLegalDocument.genratedPDFFilePath.split('/').pop();
    this.stepperApiService.downloadPdfFile(order.id, order.generatedLegalDocument.genratedPDFFilePath).subscribe(res => {
      fileSaver.saveAs(res, fileName);
    });
  }
}
