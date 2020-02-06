import { Injectable } from '@angular/core';
import { StepEntity } from 'app/entities/StepEntity';
import { StepperDataService } from 'app/stepper/stepper/stepper-data.service';
import { StepperDomManipulationService } from 'app/stepper/stepper/stepper-dom-manipulation.service';
import { MatStepper } from '@angular/material';
import { Step } from 'app/entities/Step';

@Injectable({ providedIn: 'root' })
export class StepperNavigationService {
  constructor(private stepperDataService: StepperDataService) {}

  private static getStepIndexById(stepsEntity: StepEntity[], stepId: string): number {
    let i;
    for (i = 0; i < stepsEntity.length; i++) {
      if (stepsEntity[i].id === stepId) {
        return i;
      }
    }
    return i;
  }

  static isConfirmGenerationStep(finalSteps: Step[], currentStepId: string): boolean {
    return finalSteps[0].id === currentStepId;
  }

  nextPrev(
    stepsEntity: StepEntity[],
    workflowStepsInputs: Map<string, string>[],
    workflowStepsId: string[],
    stepper: MatStepper,
    finalSteps: Step[],
    legalDocumentId: string,
    currentStepId: string,
    action: number,
    orderId: string
  ): number {
    // Next
    if (action === 1) {
      this.stepperDataService.addCurrentStepInputsValue(stepsEntity, workflowStepsInputs, currentStepId);
      let nextStepIndex = StepperNavigationService.getStepIndexById(stepsEntity, currentStepId) + 1;
      while (
        nextStepIndex < stepsEntity.length &&
        'none' === StepperDomManipulationService.getHTMLInputElement(stepsEntity[nextStepIndex].id).style.display
      ) {
        nextStepIndex++;
      }
      workflowStepsId.push(stepsEntity[nextStepIndex].id);
      stepper.selectedIndex = nextStepIndex;
      if (StepperNavigationService.isConfirmGenerationStep(finalSteps, currentStepId)) {
        this.stepperDataService.generateDocument(stepsEntity, workflowStepsInputs, legalDocumentId, orderId);
      }
    } else {
      // Previous
      workflowStepsInputs.pop();
      workflowStepsId.pop();
      stepper.selectedIndex = StepperNavigationService.getStepIndexById(stepsEntity, workflowStepsId[workflowStepsId.length - 1]);
    }
    return this.calculateProgressBarPercentage(stepsEntity, workflowStepsId[workflowStepsId.length - 1], finalSteps);
  }

  private calculateProgressBarPercentage(stepsEntity: StepEntity[], currentStepId: string, finalSteps: Step[]): number {
    let totalSteps = 0;
    let remainingSteps = 0;
    let beyondCurrentStep = false;
    stepsEntity.forEach(step => {
      if (currentStepId === step.id) {
        beyondCurrentStep = true;
      }
      if ('block' === StepperDomManipulationService.getHTMLInputElement(step.id).style.display) {
        totalSteps++;
        if (beyondCurrentStep) {
          remainingSteps++;
        }
      }
    });
    const finalStepsLength = finalSteps.length;
    return Math.round(100 - ((remainingSteps - finalStepsLength) / (totalSteps - finalStepsLength)) * 100);
  }
}
