import { Injectable } from '@angular/core';
import { StepperApiService } from 'app/stepper/stepper/stepper-api.service';
import { StepEntity } from 'app/entities/StepEntity';
import { Input } from 'app/entities/Input';

@Injectable({ providedIn: 'root' })
export class StepperDomManipulationService {
  constructor(private stepperApiService: StepperApiService) {}

  static getHTMLElement(inputId: string): HTMLElement {
    return document.getElementById(inputId);
  }

  static getHTMLInputElement(inputId: string): HTMLInputElement {
    return document.getElementById(inputId) as HTMLInputElement;
  }

  static getHTMLTextAreaElement(inputId: string): HTMLTextAreaElement {
    return document.getElementById(inputId) as HTMLTextAreaElement;
  }

  static getHTMLSelectElement(inputId: string): HTMLSelectElement {
    return document.getElementById(inputId) as HTMLSelectElement;
  }

  static getInputValue(inputId: string) {
    const htmlElement = StepperDomManipulationService.getHTMLInputElement(inputId);

    if (htmlElement.type === 'checkbox') {
      return String(htmlElement.checked);
    } else {
      return htmlElement.value;
    }
  }

  static setHTMLElementValue(inputId: string, value: string) {
    const htmlElement = StepperDomManipulationService.getHTMLElement(inputId);

    switch (htmlElement.tagName) {
      case 'INPUT':
        StepperDomManipulationService.setHtmlInputElement(inputId, value);
        break;

      case 'TEXTAREA':
        StepperDomManipulationService.getHTMLTextAreaElement(inputId).innerText = value;
        break;

      case 'SELECT':
        StepperDomManipulationService.getHTMLSelectElement(inputId).value = value;
        break;

      default:
        throw new Error('Unknown tagName : ' + htmlElement.tagName);
    }
  }

  private static setHtmlInputElement(inputId: string, value: string) {
    const htmlInputElement = StepperDomManipulationService.getHTMLInputElement(inputId);

    switch (htmlInputElement.type) {
      case 'checkbox':
        htmlInputElement.checked = Boolean(value);
        break;

      case 'radio':
        htmlInputElement.checked = true;
        break;

      default:
        htmlInputElement.value = value;
        break;
    }
  }

  applyWorkflow(inputIdsWithConstraints: string[], legalDocumentId: string, inputId: string, stepsEntity: StepEntity[]) {
    if (inputIdsWithConstraints.includes(inputId)) {
      const htmlElement = StepperDomManipulationService.getHTMLInputElement(inputId);

      if (htmlElement.type === 'radio') {
        const radioGroup = document.getElementsByName(htmlElement.name);
        for (let i = 0; i < radioGroup.length; i++) {
          if (inputId !== radioGroup[i].id) {
            this.applyConstraints(
              radioGroup[i].id,
              StepperDomManipulationService.getInputValue(radioGroup[i].id),
              legalDocumentId,
              false,
              stepsEntity
            );
          }
        }
        this.applyConstraints(inputId, StepperDomManipulationService.getInputValue(inputId), legalDocumentId, true, stepsEntity);
      } else if (htmlElement.tagName === 'SELECT') {
        const htmlSelectElement = document.getElementById(inputId) as HTMLSelectElement;
        const input = this.getInputByInputId(inputId, stepsEntity);
        if ('select_input_generator' === input.type) {
          this.applyInputGeneratorConstraints(inputId, Number(htmlSelectElement.value), legalDocumentId);
        } else {
          for (let i = 0; i < htmlSelectElement.options.length; i++) {
            if (htmlSelectElement.value !== htmlSelectElement.options[i].value) {
              this.applyConstraints(inputId, htmlSelectElement.options[i].value, legalDocumentId, false, stepsEntity);
            }
          }
        }
        this.applyConstraints(inputId, htmlSelectElement.value, legalDocumentId, true, stepsEntity);
      } else {
        this.applyConstraints(inputId, StepperDomManipulationService.getInputValue(inputId), legalDocumentId, true, stepsEntity);
      }
    }
  }

  private applyConstraints(
    inputId: string,
    inputValue: string,
    legalDocumentId: string,
    toggleConstraints: boolean,
    stepsEntity: StepEntity[]
  ) {
    const nbOccurence: number = +inputValue;
    let inptVal: string = inputValue;
    const input: Input = this.getInputByInputId(inputId, stepsEntity);
    if ('select_input_generator' === input.type) {
      inptVal = 'select_input_generator';
    }
    this.stepperApiService.getConstraints(inputId, inptVal, legalDocumentId).subscribe(res => {
      let valuesConstraint = res.body;

      if ('select_input_generator' === input.type) {
        valuesConstraint = valuesConstraint.slice(0, nbOccurence); // exculsive
      }

      valuesConstraint.forEach(function(constraint) {
        let elementId: string;

        // If element is an input hide the div containing label+input to hide
        if (
          StepperDomManipulationService.getHTMLInputElement(constraint.elementId).tagName === 'INPUT' ||
          StepperDomManipulationService.getHTMLInputElement(constraint.elementId).tagName === 'TEXTAREA' ||
          StepperDomManipulationService.getHTMLInputElement(constraint.elementId).tagName === 'SELECT'
        ) {
          elementId = 'InputDiv' + constraint.elementId;
        } else {
          elementId = constraint.elementId;
        }

        // Specify to apply the constraints or the opposite of them
        if (toggleConstraints) {
          StepperDomManipulationService.getHTMLInputElement(elementId).setAttribute(
            'style',
            'display:' + (constraint.visibility ? 'block' : 'none')
          );
        } else {
          StepperDomManipulationService.getHTMLInputElement(elementId).setAttribute(
            'style',
            'display:' + (constraint.visibility ? 'none' : 'block')
          );
        }
      });
    });
  }

  private applyInputGeneratorConstraints(inputId: string, nbOccurence: number, legalDocumentId: string) {
    this.stepperApiService.getConstraints(inputId, 'select_input_generator', legalDocumentId).subscribe(res => {
      const valuesContraint = res.body;
      let i = 0;
      valuesContraint.forEach(function(constraint) {
        let elementId: string;

        // If element is an input hide the div containing label+input to hide
        if (
          StepperDomManipulationService.getHTMLInputElement(constraint.elementId).tagName === 'INPUT' ||
          StepperDomManipulationService.getHTMLInputElement(constraint.elementId).tagName === 'TEXTAREA' ||
          StepperDomManipulationService.getHTMLInputElement(constraint.elementId).tagName === 'SELECT'
        ) {
          elementId = 'InputDiv' + constraint.elementId;
        } else {
          elementId = constraint.elementId;
        }
        if (i < nbOccurence) {
          StepperDomManipulationService.getHTMLInputElement(elementId).setAttribute('style', 'display:block');
        } else {
          StepperDomManipulationService.getHTMLInputElement(elementId).setAttribute('style', 'display:none');
        }
        i++;
      });
    });
  }

  private getInputByInputId(inputId: string, stepsEntity: StepEntity[]): Input {
    let inputReturned: Input = null;
    stepsEntity.forEach(step => {
      step.blocs.forEach(bloc => {
        bloc.inputs.forEach(input => {
          if (input.id === inputId) {
            inputReturned = input;
          }
        });
      });
    });
    return inputReturned;
  }
}
