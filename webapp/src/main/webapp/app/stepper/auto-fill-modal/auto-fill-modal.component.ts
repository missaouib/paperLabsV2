import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { LegalDocument } from 'app/shared/model/legal-document.model';
import { AutoFillModalService } from 'app/stepper/auto-fill-modal/auto-fill-modal.service';
import { Company } from 'app/shared/model/company.model';
import { Employer } from 'app/shared/model/employer.model';

@Component({
  selector: 'jhi-auto-fill-modal',
  templateUrl: './auto-fill-modal.component.html',
  styleUrls: ['./auto-fill-modal.component.scss']
})
export class JhiAutoFillModalComponent implements OnInit {
  autoFillConcernedEntities: string[] = [];
  autoFillConcernedEntitiesDisplayableName: string[] = [];
  userLogin: string;
  autoFillCompanySectionEnabled = true;
  autoFillEmployerSectionEnabled = true;
  legalDocument: LegalDocument;
  userCompanies: Company[] = [];
  userEmployers: Employer[] = [];
  userChosenCompanyNames: any[] = [];
  userChosenEmployerNames: string[] = [];

  constructor(public activeModal: NgbActiveModal, private autoFillModalService: AutoFillModalService) {}

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

  private static setHtmlInputElement(inputId: string, value: string) {
    const htmlInputElement = JhiAutoFillModalComponent.getHTMLInputElement(inputId);

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

  static setHTMLElementValue(inputId: string, value: string) {
    
    const htmlElement = JhiAutoFillModalComponent.getHTMLElement(inputId);
    if (!htmlElement)
      return;
    switch (htmlElement.tagName) {
      case 'INPUT':
        JhiAutoFillModalComponent.setHtmlInputElement(inputId, value);
        break;

      case 'TEXTAREA':
        JhiAutoFillModalComponent.getHTMLTextAreaElement(inputId).innerText = value;
        break;

      case 'SELECT':
        JhiAutoFillModalComponent.getHTMLSelectElement(inputId).value = value;
        break;

      default:
        throw new Error('Unknown tagName : ' + htmlElement.tagName);
    }
  }

  ngOnInit() {
    this.autoFillModalService.currentLegalDocument.subscribe(res => {
      this.legalDocument = res;
    });
    this.autoFillConcernedEntities = this.legalDocument.autoFillConcernedEntities;
    this.transformAutoFillConcernedEntitiesToDisplayableName();
    this.autoFillModalService.currentUserLogin.subscribe(userLogin => {
      this.userLogin = userLogin;
    });
    this.getCurrentUserConcernedEntityObjects();
  }

  submit() {
    const userChosenCompanies: Company[] = [];
    const userChosenEmployers: Employer[] = [];

    if (this.userCompanies.length > 0) {
      this.userCompanies.forEach(comp => {
        if (this.userChosenCompanyNames.includes(comp.companyName)) {
          userChosenCompanies.push(comp);
        }
      });
    }
    if (this.userEmployers.length > 0) {
      this.userEmployers.forEach(emp => {
        if (this.userChosenEmployerNames.includes(emp.employerFullName)) {
          userChosenEmployers.push(emp);
        }
      });
    }
    if (userChosenCompanies.length > 0) {
      for (let i = 0; i < userChosenCompanies.length; i++) {
        const companyAutoFillInputIdsByFieldName: any = this.legalDocument.companiesAutoFillInputIdsByFieldName[i];
        const userChosenCompany: any = userChosenCompanies[i];
        Object.keys(companyAutoFillInputIdsByFieldName).forEach(key => {
          JhiAutoFillModalComponent.setHTMLElementValue(companyAutoFillInputIdsByFieldName[key], userChosenCompany[key]);
        });
      }
    }
    if (userChosenEmployers.length > 0) {
      for (let i = 0; i < userChosenEmployers.length; i++) {
        const employerAutoFillInputIdsByFieldName: any = this.legalDocument.employersAutoFillInputIdsByFieldName[i];
        const userChosenEmployer: any = userChosenEmployers[i];
        Object.keys(employerAutoFillInputIdsByFieldName).forEach(key => {
          JhiAutoFillModalComponent.setHTMLElementValue(employerAutoFillInputIdsByFieldName[key], userChosenEmployer[key]);
        });
      }
    }
    this.activeModal.dismiss();
  }

  getCurrentUserConcernedEntityObjects() {
    this.autoFillConcernedEntities.forEach(autoFillConcernedEntity => {
      if (autoFillConcernedEntity === 'Company') {
        this.autoFillModalService.searchCompaniesByUser(this.userLogin).subscribe(res => {
          this.userCompanies = res.body;
        });
      } else if (autoFillConcernedEntity === 'Employer') {
        this.autoFillModalService.searchEmployersByUser(this.userLogin).subscribe(res => {
          this.userEmployers = res.body;
        });
      }
    });
  }

  transformAutoFillConcernedEntitiesToDisplayableName() {
    this.autoFillConcernedEntities.forEach(autoFillConcernedEntity => {
      if (autoFillConcernedEntity === 'Company') {
        this.autoFillConcernedEntitiesDisplayableName.push('Société');
      } else if (autoFillConcernedEntity === 'Employer') {
        this.autoFillConcernedEntitiesDisplayableName.push('Employeur');
      }
    });
  }

  clearDataList(inputId: string) {
    JhiAutoFillModalComponent.setHtmlInputElement(inputId, '');
    if ('companiesDataList' === inputId) {
      this.userChosenCompanyNames = [];
    } else if ('employersDataList' === inputId) {
      this.userChosenEmployerNames = [];
    }
  }
}
