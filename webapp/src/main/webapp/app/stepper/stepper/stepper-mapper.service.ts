import { Injectable } from '@angular/core';
import { StepEntity } from 'app/entities/StepEntity';
import { BlocEntity } from 'app/entities/BlocEntity';
import { Input } from 'app/entities/Input';
import { Bloc } from 'app/entities/Bloc';
import { StepperConfig } from 'app/entities/StepperConfig';

@Injectable({ providedIn: 'root' })
export class StepperMapperService {
  constructor() {}

  private static getBloc(blocId: string, stepperConfig: StepperConfig): Bloc {
    for (let i = 0; i < stepperConfig.blocs.length; i++) {
      if (stepperConfig.blocs[i].id === blocId) {
        return stepperConfig.blocs[i];
      }
    }
  }

  private static getInput(inputId: string, stepperConfig: StepperConfig): Input {
    for (let i = 0; i < stepperConfig.inputs.length; i++) {
      if (inputId === stepperConfig.inputs[i].id) {
        return stepperConfig.inputs[i];
      }
    }
  }

  getStepperEntity(stepperConfig: StepperConfig): StepEntity[] {
    const stepsEntity: StepEntity[] = [];

    stepperConfig.steps.forEach(step => {
      const stepEntity: StepEntity = new StepEntity();
      stepEntity.id = step.id;
      stepEntity.title = step.title;
      stepEntity.description = step.description;
      stepEntity.visibility = step.visibility;
      stepEntity.blocs = [];

      step.blocs.forEach(blocId => {
        const bloc = StepperMapperService.getBloc(blocId, stepperConfig);
        const blocEntity: BlocEntity = new BlocEntity();
        blocEntity.id = bloc.id;
        blocEntity.title = bloc.title;
        blocEntity.description = bloc.description;
        blocEntity.visibility = bloc.visibility;
        blocEntity.inputs = [];

        bloc.inputs.forEach(inputId => {
          const input = StepperMapperService.getInput(inputId, stepperConfig);
          blocEntity.inputs.push(input);
        });

        stepEntity.blocs.push(blocEntity);
      });
      stepsEntity.push(stepEntity);
    });

    return stepsEntity;
  }
}
