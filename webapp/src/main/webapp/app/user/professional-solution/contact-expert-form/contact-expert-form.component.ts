import { Component, OnInit } from '@angular/core';
import { Contact, IContact } from 'app/entities/Contact';
import { FormBuilder, Validators } from '@angular/forms';
import { ContactService } from 'app/user/contact/contact.service';

@Component({
  selector: 'jhi-contact-expert-form',
  templateUrl: './contact-expert-form.component.html',
  styleUrls: ['./contact-expert-form.component.scss']
})
export class ContactExpertFormComponent implements OnInit {
  success: boolean;
  error: string;
  contact: IContact;

  contactExpertForm = this.formBuilder.group({
    firstName: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), Validators.pattern('^[ _.@A-Za-z0-9-]*$')]],
    lastName: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), Validators.pattern('^[ _.@A-Za-z0-9-]*$')]],
    phoneNumber: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    company: ['']
  });

  constructor(private formBuilder: FormBuilder, private contactService: ContactService) {}

  ngOnInit() {
    this.success = false;
    this.contact = new Contact();
  }

  sendContactExpertEmail() {
    this.getInputValues();
    this.contactService.sendContactExpertEmail(this.contact).subscribe(
      () => {
        this.success = true;
        this.error = null;
      },
      () => this.processError()
    );
  }

  private getInputValues() {
    this.contact.firstName = this.contactExpertForm.get(['firstName']).value;
    this.contact.lastName = this.contactExpertForm.get(['lastName']).value;
    this.contact.phoneNumber = this.contactExpertForm.get(['phoneNumber']).value;
    this.contact.email = this.contactExpertForm.get(['email']).value;
    this.contact.company = this.contactExpertForm.get(['company']).value;
  }

  private processError() {
    this.success = null;
    this.error = 'ERROR';
  }
}
