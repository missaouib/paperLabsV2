import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormArray, FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ILegalDocument, LegalDocument } from 'app/shared/model/legal-document.model';
import { LegalDocumentService } from './legal-document.service';

@Component({
  selector: 'jhi-legal-document-update',
  templateUrl: './legal-document-update.component.html'
})
export class LegalDocumentUpdateComponent implements OnInit {
  isSaving: boolean;
  allLegalDocumentsOtherThanThis: ILegalDocument[] = [];
  legalDocument: LegalDocument;

  editForm = this.fb.group({
    id: [],
    shortName: [null, [Validators.required]],
    fullName: [null, [Validators.required]],
    keywords: [],
    description: [null, [Validators.required]],
    templatePreviewPath: [null, [Validators.required]],
    templateFilePath: [null, [Validators.required]],
    stepperConfigFilePath: [null, [Validators.required]],
    workflowConfigFilePath: [null, [Validators.required]],
    price: [],
    documentsRecommendation: new FormArray([])
  });

  constructor(protected legalDocumentService: LegalDocumentService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ legalDocument }) => {
      this.legalDocument = legalDocument;
      this.loadAll();
      this.updateForm(legalDocument);
    });
  }

  private addCheckboxes() {
    this.allLegalDocumentsOtherThanThis.forEach(o => {
      let control;
      let found = false;
      if (this.legalDocument !== undefined && this.legalDocument.documentsRecommendation !== undefined) {
        this.legalDocument.documentsRecommendation.forEach(document => {
          if (document.id === o.id) {
            control = new FormControl(true);
            found = true;
          }
        });
        if (found === false) {
          control = new FormControl(false);
        }
      } else {
        control = new FormControl(false);
      }
      (this.editForm.controls.documentsRecommendation as FormArray).push(control);
    });
  }

  loadAll() {
    this.legalDocumentService.query({}).subscribe((res: HttpResponse<ILegalDocument[]>) => {
      this.allLegalDocumentsOtherThanThis = res.body.filter(item => item.shortName !== this.legalDocument.shortName); // remove actual legal document from array of recommendations
      this.addCheckboxes();
    });
  }

  get documentsRecommendation() {
    return (this.editForm.get('documentsRecommendation') as FormArray).controls;
  }

  updateForm(legalDocument: ILegalDocument) {
    this.editForm.patchValue({
      id: legalDocument.id,
      shortName: legalDocument.shortName,
      fullName: legalDocument.fullName,
      keywords: legalDocument.keywords,
      description: legalDocument.description,
      templatePreviewPath: legalDocument.templatePreviewPath,
      templateFilePath: legalDocument.templateFilePath,
      stepperConfigFilePath: legalDocument.stepperConfigFilePath,
      workflowConfigFilePath: legalDocument.workflowConfigFilePath,
      price: legalDocument.price,
      documentsRecommendation: legalDocument.documentsRecommendation
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;

    const documentsRecommendation: string[] = this.editForm.value.documentsRecommendation
      .map((v, i) => (v ? this.allLegalDocumentsOtherThanThis[i] : null))
      .filter(v => v !== null);

    const legalDocument = this.createFromForm(this.legalDocument, documentsRecommendation);

    if (legalDocument.id !== undefined) {
      this.subscribeToSaveResponse(this.legalDocumentService.update(legalDocument));
    } else {
      this.subscribeToSaveResponse(this.legalDocumentService.create(legalDocument));
    }
  }

  private createFromForm(legalDocument: ILegalDocument, documentsRecommendation: string[]): ILegalDocument {
    return {
      ...new LegalDocument(),
      id: this.editForm.get(['id']).value,
      shortName: this.editForm.get(['shortName']).value,
      fullName: this.editForm.get(['fullName']).value,
      keywords: this.editForm.get(['keywords']).value,
      description: this.editForm.get(['description']).value,
      templatePreviewPath: this.editForm.get(['templatePreviewPath']).value,
      templateFilePath: this.editForm.get(['templateFilePath']).value,
      stepperConfigFilePath: this.editForm.get(['stepperConfigFilePath']).value,
      workflowConfigFilePath: this.editForm.get(['workflowConfigFilePath']).value,
      price: this.editForm.get(['price']).value,
      descriptionSections: legalDocument !== undefined ? legalDocument.descriptionSections : null,
      documentsRecommendation,
      autoFillConcernedEntities: legalDocument !== undefined ? legalDocument.autoFillConcernedEntities : [],
      companiesAutoFillInputIdsByFieldName: legalDocument !== undefined ? legalDocument.companiesAutoFillInputIdsByFieldName : null,
      employersAutoFillInputIdsByFieldName: legalDocument !== undefined ? legalDocument.employersAutoFillInputIdsByFieldName : null,
      categoryId: legalDocument !== undefined ? legalDocument.categoryId : null,
      lawyerId: legalDocument !== undefined ? legalDocument.categoryId : null
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILegalDocument>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
