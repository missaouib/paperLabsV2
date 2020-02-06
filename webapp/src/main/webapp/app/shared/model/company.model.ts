export interface ICompany {
  id?: string;
  companyName?: string;
  companyCapital?: number;
  companyUniqueIdentification?: string;
  companyRepresentativeFullName?: string;
  companyFullAddress?: string;
  companyType?: string;
}

export class Company implements ICompany {
  constructor(
    public id?: string,
    public companyName?: string,
    public companyCapital?: number,
    public companyUniqueIdentification?: string,
    public companyRepresentativeFullName?: string,
    public companyFullAddress?: string,
    public companyType?: string
  ) {}
}
