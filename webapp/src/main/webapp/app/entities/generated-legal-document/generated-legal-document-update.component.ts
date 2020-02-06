import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IGeneratedLegalDocument, GeneratedLegalDocument } from 'app/shared/model/generated-legal-document.model';
import { GeneratedLegalDocumentService } from './generated-legal-document.service';

@Component({
  selector: 'jhi-generated-legal-document-update',
  templateUrl: './generated-legal-document-update.component.html'
})
export class GeneratedLegalDocumentUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    generatedWordFilePath: [null, [Validators.required]],
    genratedPDFFilePath: [],
    date: []
  });

  constructor(
    protected generatedLegalDocumentService: GeneratedLegalDocumentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ generatedLegalDocument }) => {
      this.updateForm(generatedLegalDocument);
    });
  }

  updateForm(generatedLegalDocument: IGeneratedLegalDocument) {
    this.editForm.patchValue({
      id: generatedLegalDocument.id,
      generatedWordFilePath: generatedLegalDocument.generatedWordFilePath,
      genratedPDFFilePath: generatedLegalDocument.genratedPDFFilePath,
      date: generatedLegalDocument.date != null ? generatedLegalDocument.date.format(DATE_TIME_FORMAT) : null
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const generatedLegalDocument = this.createFromForm();
    if (generatedLegalDocument.id !== undefined) {
      this.subscribeToSaveResponse(this.generatedLegalDocumentService.update(generatedLegalDocument));
    } else {
      this.subscribeToSaveResponse(this.generatedLegalDocumentService.create(generatedLegalDocument));
    }
  }

  private createFromForm(): IGeneratedLegalDocument {
    return {
      ...new GeneratedLegalDocument(),
      id: this.editForm.get(['id']).value,
      generatedWordFilePath: this.editForm.get(['generatedWordFilePath']).value,
      genratedPDFFilePath: this.editForm.get(['genratedPDFFilePath']).value,
      date: this.editForm.get(['date']).value != null ? moment(this.editForm.get(['date']).value, DATE_TIME_FORMAT) : undefined
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGeneratedLegalDocument>>) {
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
