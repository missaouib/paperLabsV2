import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IOrder, Order } from 'app/shared/model/order.model';
import { OrderService } from './order.service';

@Component({
  selector: 'jhi-order-update',
  templateUrl: './order-update.component.html'
})
export class OrderUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    paymentMethod: [],
    totalPrice: [null, [Validators.required]],
    price: [null, [Validators.required]],
    tax: [],
    invoiceFilePath: [],
    status: [],
    destinationEmail: []
  });

  constructor(protected orderService: OrderService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ order }) => {
      this.updateForm(order);
    });
  }

  updateForm(order: IOrder) {
    this.editForm.patchValue({
      id: order.id,
      paymentMethod: order.paymentMethod,
      totalPrice: order.totalPrice,
      price: order.price,
      tax: order.tax,
      invoiceFilePath: order.invoiceFilePath,
      status: order.status,
      destinationEmail: order.destinationEmail
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const order = this.createFromForm();
    if (order.id !== undefined) {
      this.subscribeToSaveResponse(this.orderService.update(order));
    } else {
      this.subscribeToSaveResponse(this.orderService.create(order));
    }
  }

  private createFromForm(): IOrder {
    return {
      ...new Order(),
      id: this.editForm.get(['id']).value,
      paymentMethod: this.editForm.get(['paymentMethod']).value,
      totalPrice: this.editForm.get(['totalPrice']).value,
      price: this.editForm.get(['price']).value,
      tax: this.editForm.get(['tax']).value,
      invoiceFilePath: this.editForm.get(['invoiceFilePath']).value,
      status: this.editForm.get(['status']).value,
      destinationEmail: this.editForm.get(['destinationEmail']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrder>>) {
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
