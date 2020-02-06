import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IEmployee, Employee } from 'app/shared/model/employee.model';
import { EmployeeService } from './employee.service';

@Component({
  selector: 'jhi-employee-update',
  templateUrl: './employee-update.component.html'
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    employeeFullName: [],
    employeeCinNumber: [],
    employeeCinDeliveredDate: [],
    employeeCinDeliveredLocation: [],
    employeeFullAddress: [],
    employeePostion: []
  });

  constructor(protected employeeService: EmployeeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.updateForm(employee);
    });
  }

  updateForm(employee: IEmployee) {
    this.editForm.patchValue({
      id: employee.id,
      employeeFullName: employee.employeeFullName,
      employeeCinNumber: employee.employeeCinNumber,
      employeeCinDeliveredDate: employee.employeeCinDeliveredDate,
      employeeCinDeliveredLocation: employee.employeeCinDeliveredLocation,
      employeeFullAddress: employee.employeeFullAddress,
      employeePostion: employee.employeePostion
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const employee = this.createFromForm();
    if (employee.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  private createFromForm(): IEmployee {
    return {
      ...new Employee(),
      id: this.editForm.get(['id']).value,
      employeeFullName: this.editForm.get(['employeeFullName']).value,
      employeeCinNumber: this.editForm.get(['employeeCinNumber']).value,
      employeeCinDeliveredDate: this.editForm.get(['employeeCinDeliveredDate']).value,
      employeeCinDeliveredLocation: this.editForm.get(['employeeCinDeliveredLocation']).value,
      employeeFullAddress: this.editForm.get(['employeeFullAddress']).value,
      employeePostion: this.editForm.get(['employeePostion']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>) {
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
