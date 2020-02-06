import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ContactService } from 'app/user/contact/contact.service';
import { Contact, IContact } from 'app/entities/Contact';

@Component({
  selector: 'jhi-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.scss']
})
export class ContactComponent implements OnInit {
  success: boolean;
  error: string;
  contact: IContact;

  contactForm = this.formBuilder.group({
    firstName: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), Validators.pattern('^[ _.@A-Za-z0-9-]*$')]],
    lastName: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), Validators.pattern('^[ _.@A-Za-z0-9-]*$')]],
    email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    need: ['', [Validators.required]],
    message: ['']
  });

  constructor(private formBuilder: FormBuilder, private contactService: ContactService) {}

  ngOnInit() {
    this.success = false;
    this.contact = new Contact();
  }

  sendContactEmail() {
    this.getInputValues();
    this.contactService.sendContactEmail(this.contact).subscribe(
      () => {
        this.success = true;
        this.error = null;
      },
      () => this.processError()
    );
  }

  private getInputValues() {
    this.contact.firstName = this.contactForm.get(['firstName']).value;
    this.contact.lastName = this.contactForm.get(['lastName']).value;
    this.contact.email = this.contactForm.get(['email']).value;
    this.contact.need = this.contactForm.get(['need']).value;
    this.contact.message = this.contactForm.get(['message']).value;
  }

  private processError() {
    this.success = null;
    this.error = 'ERROR';
  }
}
