import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatStepper } from '@angular/material';
import { StepperApiService } from 'app/stepper/stepper/stepper-api.service';
import { ActivatedRoute } from '@angular/router';
import { ILegalDocument } from 'app/shared/model/legal-document.model';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AutoFillModalService } from 'app/stepper/auto-fill-modal/auto-fill-modal.service';
import { JhiAutoFillModalComponent } from 'app/stepper/auto-fill-modal/auto-fill-modal.component';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { StepEntity } from 'app/entities/StepEntity';
import { StepperEventManagerService } from 'app/stepper/stepper/stepper-event-manager.service';
import { StepperConfig } from 'app/entities/StepperConfig';
import { StepperMapperService } from 'app/stepper/stepper/stepper-mapper.service';
import { Subscription } from 'rxjs';
import { StepperNavigationService } from 'app/stepper/stepper/stepper-navigation.service';
import { StepperDomManipulationService } from 'app/stepper/stepper/stepper-dom-manipulation.service';
import { Step } from 'app/entities/Step';

@Component({
  selector: 'jhi-stepper',
  templateUrl: './stepper.component.html',
  styleUrls: ['stepper.scss']
})
export class StepperComponent implements OnInit, OnDestroy {
  modalRef: NgbModalRef;
  private isOpen = false;

  @ViewChild('stepper', { static: false }) stepper: MatStepper;

  stepperConfig: StepperConfig;
  stepsEntity: StepEntity[] = [];

  legalDocument: ILegalDocument;
  workflowStepsId: string[] = [];
  inputIdsWithConstraints: string[] = [];
  workflowStepsInputs: Map<string, string>[] = [];
  progressBar = 0;
  finalSteps: Step[] = [];
  nextPrevSubscriber: Subscription;
  onChangeSubscriber: Subscription;

  constructor(
    private route: ActivatedRoute,
    private autoFillModalService: AutoFillModalService,
    private stepperApiService: StepperApiService,
    private stepperEventManagerService: StepperEventManagerService,
    private stepperMapperService: StepperMapperService,
    private stepperNavigationService: StepperNavigationService,
    private stepperDomManipulationService: StepperDomManipulationService,
    private modalService: NgbModal,
    private accountService: AccountService
  ) {}

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  ngOnInit() {
    this.route.data.subscribe(({ legalDocument }) => {
      this.legalDocument = legalDocument;
      this.sendDataToAutoFillModalService();

      this.finalSteps = [
        {
          id: 'step_confirmer_generation',
          title: 'Générer le document',
          description: '',
          blocs: [],
          visibility: true
        },
        {
          id: 'step_confirmer_paiement',
          title: 'Votre "' + this.legalDocument.fullName + '" est prêt',
          description: '',
          blocs: [],
          visibility: true
        }
      ];

      this.stepperApiService.getStepperConfig(this.legalDocument.id).subscribe(res => {
        this.stepperConfig = res.body;

        // Add final steps
        this.stepperConfig.steps.push(...this.finalSteps);

        this.stepsEntity = this.stepperMapperService.getStepperEntity(this.stepperConfig);

        // Init workflow with the first step
        if (this.stepsEntity.length > 0) {
          this.workflowStepsId.push(this.stepsEntity[0].id);
        }
      });
    });

    this.stepperApiService.getInputIdsWithConstraints(this.legalDocument.id).subscribe(res => {
      this.inputIdsWithConstraints = res.body;
    });

    if (this.isAuthenticated()) {
      this.accountService.identity().then((account: Account) => {
        this.sendUserLoginToFillModalService(account.email);
        this.autoFillModalService.currentUserHasConcernedEntityObjects(this.legalDocument.id, account.email).subscribe(res => {
          if (res.body && this.legalDocument.shortName !== 'PV-NNG' && this.legalDocument.shortName !== 'SARL') {
            this.modalRef = this.open();
          }
        });
      });
    }

    this.nextPrevSubscriber = this.stepperEventManagerService.getNextPrev().subscribe(res => {
      this.nextPrev(res.currentStepId, res.action, res.orderId);
    });

    this.onChangeSubscriber = this.stepperEventManagerService.getOnChange().subscribe(res => {
      this.applyWorkflow(res, this.stepsEntity);
    });
  }

  applyWorkflow(inputId: string, stepsEntity: StepEntity[]) {
    this.stepperDomManipulationService.applyWorkflow(this.inputIdsWithConstraints, this.legalDocument.id, inputId, stepsEntity);
  }

  isFirstStep(currentStepId: string): boolean {
    return this.stepsEntity[0].id === currentStepId;
  }

  isConfirmGenerationStep(currentStepId: string): boolean {
    return StepperNavigationService.isConfirmGenerationStep(this.finalSteps, currentStepId);
  }

  isFinalStep(currentStepId: string): boolean {
    return this.stepsEntity[this.stepsEntity.length - 1].id === currentStepId;
  }

  nextPrev(currentStepId: string, action: number, orderId: string) {
    this.progressBar = this.stepperNavigationService.nextPrev(
      this.stepsEntity,
      this.workflowStepsInputs,
      this.workflowStepsId,
      this.stepper,
      this.finalSteps,
      this.legalDocument.id,
      currentStepId,
      action,
      orderId
    );
  }

  ngOnDestroy() {
    this.nextPrevSubscriber.unsubscribe();
    this.onChangeSubscriber.unsubscribe();
  }

  open(): NgbModalRef {
    if (this.isOpen) {
      return;
    }
    this.isOpen = true;
    const modalRef = this.modalService.open(JhiAutoFillModalComponent);
    modalRef.result.then(
      result => {
        this.isOpen = false;
      },
      reason => {
        this.isOpen = false;
      }
    );
    return modalRef;
  }

  sendDataToAutoFillModalService() {
    this.autoFillModalService.setLegalDocument(this.legalDocument);
  }

  sendUserLoginToFillModalService(userLogin: string) {
    this.autoFillModalService.setUserLogin(userLogin);
  }
}
