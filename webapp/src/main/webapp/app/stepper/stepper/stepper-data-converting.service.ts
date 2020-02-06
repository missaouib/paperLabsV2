import { Injectable } from '@angular/core';
import { StepEntity } from 'app/entities/StepEntity';

@Injectable({ providedIn: 'root' })
export class StepperDataConvertingService {
  convert(stepsEntity: StepEntity[], workflowStepsInputs: Map<string, string>[], legalDocumentId: string) {
    // Regroup all inputs to map object
    const stepperDataMap = this.regroupStepperDataToMap(workflowStepsInputs, legalDocumentId);

    return this.convertDataToNestedObject(stepperDataMap);
  }

  private regroupStepperDataToMap(workflowStepsInputs: Map<string, string>[], legalDocumentId: string): Map<string, string> {
    const stepperDataMap = new Map<string, string>();
    workflowStepsInputs.forEach(stepInputsValue => {
      stepInputsValue.forEach((value, inputId) => {
        stepperDataMap.set(inputId, value);
      });
    });
    // Add legalDocumentId to StepperData for calculating inputs by legalDocumentId
    stepperDataMap.set('LEGAL_DOCUMENT_ID', legalDocumentId);
    return stepperDataMap;
  }

  private convertDataToNestedObject(stepperDataMap: Map<string, string>): any {
    const stepperData = {};
    stepperDataMap.forEach((value, inputId) => {
      stepperData[inputId] = {
        id: inputId,
        value
      };
    });
    return stepperData;
  }
}
