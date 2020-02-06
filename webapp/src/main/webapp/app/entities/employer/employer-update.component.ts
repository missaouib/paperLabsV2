import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IEmployer, Employer } from 'app/shared/model/employer.model';
import { EmployerService } from './employer.service';

@Component({
  selector: 'jhi-employer-update',
  templateUrl: './employer-update.component.html'
})
export class EmployerUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    employerCinNumber: [],
    employerFullName: [],
    employerFullAddress: []
  });

  constructor(protected employerService: EmployerService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ employer }) => {
      this.updateForm(employer);
    });
  }

  updateForm(employer: IEmployer) {
    this.editForm.patchValue({
      id: employer.id,
      employerCinNumber: employer.employerCinNumber,
      employerFullName: employer.employerFullName,
      employerFullAddress: employer.employerFullAddress
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const employer = this.createFromForm();
    if (employer.id !== undefined) {
      this.subscribeToSaveResponse(this.employerService.update(employer));
    } else {
      this.subscribeToSaveResponse(this.employerService.create(employer));
    }
  }

  private createFromForm(): IEmployer {
    return {
      ...new Employer(),
      id: this.editForm.get(['id']).value,
      employerCinNumber: this.editForm.get(['employerCinNumber']).value,
      employerFullName: this.editForm.get(['employerFullName']).value,
      employerFullAddress: this.editForm.get(['employerFullAddress']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployer>>) {
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
