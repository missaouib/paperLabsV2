export interface ILawyer {
  id?: string;
  firstName?: string;
  lastName?: string;
  mobile?: string;
  tel?: string;
  email?: string;
  address?: string;
}

export class Lawyer implements ILawyer {
  constructor(
    public id?: string,
    public firstName?: string,
    public lastName?: string,
    public mobile?: string,
    public tel?: string,
    public email?: string,
    public address?: string
  ) {}
}
