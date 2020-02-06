import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ILawyer, Lawyer } from 'app/shared/model/lawyer.model';
import { LawyerService } from './lawyer.service';

@Component({
  selector: 'jhi-lawyer-update',
  templateUrl: './lawyer-update.component.html'
})
export class LawyerUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    firstName: [null, [Validators.required]],
    lastName: [null, [Validators.required]],
    mobile: [],
    tel: [],
    email: [],
    address: []
  });

  constructor(protected lawyerService: LawyerService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ lawyer }) => {
      this.updateForm(lawyer);
    });
  }

  updateForm(lawyer: ILawyer) {
    this.editForm.patchValue({
      id: lawyer.id,
      firstName: lawyer.firstName,
      lastName: lawyer.lastName,
      mobile: lawyer.mobile,
      tel: lawyer.tel,
      email: lawyer.email,
      address: lawyer.address
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const lawyer = this.createFromForm();
    if (lawyer.id !== undefined) {
      this.subscribeToSaveResponse(this.lawyerService.update(lawyer));
    } else {
      this.subscribeToSaveResponse(this.lawyerService.create(lawyer));
    }
  }

  private createFromForm(): ILawyer {
    return {
      ...new Lawyer(),
      id: this.editForm.get(['id']).value,
      firstName: this.editForm.get(['firstName']).value,
      lastName: this.editForm.get(['lastName']).value,
      mobile: this.editForm.get(['mobile']).value,
      tel: this.editForm.get(['tel']).value,
      email: this.editForm.get(['email']).value,
      address: this.editForm.get(['address']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILawyer>>) {
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
