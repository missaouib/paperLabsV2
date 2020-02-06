import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IDescriptionSection, DescriptionSection } from 'app/shared/model/description-section.model';
import { DescriptionSectionService } from './description-section.service';

@Component({
  selector: 'jhi-description-section-update',
  templateUrl: './description-section-update.component.html'
})
export class DescriptionSectionUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    content: [null, [Validators.required]]
  });

  constructor(
    protected descriptionSectionService: DescriptionSectionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ descriptionSection }) => {
      this.updateForm(descriptionSection);
    });
  }

  updateForm(descriptionSection: IDescriptionSection) {
    this.editForm.patchValue({
      id: descriptionSection.id,
      title: descriptionSection.title,
      content: descriptionSection.content
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const descriptionSection = this.createFromForm();
    if (descriptionSection.id !== undefined) {
      this.subscribeToSaveResponse(this.descriptionSectionService.update(descriptionSection));
    } else {
      this.subscribeToSaveResponse(this.descriptionSectionService.create(descriptionSection));
    }
  }

  private createFromForm(): IDescriptionSection {
    return {
      ...new DescriptionSection(),
      id: this.editForm.get(['id']).value,
      title: this.editForm.get(['title']).value,
      content: this.editForm.get(['content']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDescriptionSection>>) {
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
