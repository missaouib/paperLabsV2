export interface IContact {
  firstName?: string;
  lastName?: string;
  email?: string;
  need?: string;
  message?: string;
  phoneNumber?: string;
  company?: string;
}

export class Contact implements IContact {
  constructor(
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public need?: string,
    public message?: string,
    public phoneNumber?: string,
    public company?: string
  ) {}
}
