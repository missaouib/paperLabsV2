export interface IEmployer {
  id?: string;
  employerCinNumber?: number;
  employerFullName?: string;
  employerFullAddress?: string;
}

export class Employer implements IEmployer {
  constructor(
    public id?: string,
    public employerCinNumber?: number,
    public employerFullName?: string,
    public employerFullAddress?: string
  ) {}
}
