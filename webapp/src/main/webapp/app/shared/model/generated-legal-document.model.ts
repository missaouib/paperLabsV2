import { Moment } from 'moment';

export interface IGeneratedLegalDocument {
  id?: string;
  generatedWordFilePath?: string;
  genratedPDFFilePath?: string;
  date?: Moment;
  paymentDate?: Moment;
}

export class GeneratedLegalDocument implements IGeneratedLegalDocument {
  constructor(
    public id?: string,
    public generatedWordFilePath?: string,
    public genratedPDFFilePath?: string,
    public date?: Moment,
    public paymentDate?: Moment
  ) {}
}
