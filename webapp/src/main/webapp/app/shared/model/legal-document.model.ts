import { IDescriptionSection } from 'app/shared/model/description-section.model';

export interface ILegalDocument {
  id?: string;
  shortName?: string;
  fullName?: string;
  keywords?: string;
  description?: string;
  templatePreviewPath?: string;
  templateFilePath?: string;
  stepperConfigFilePath?: string;
  workflowConfigFilePath?: string;
  descriptionSections?: IDescriptionSection[];
  price?: number;
  autoFillConcernedEntities?: string[];
  companiesAutoFillInputIdsByFieldName?: any[];
  employersAutoFillInputIdsByFieldName?: any[];
  documentsRecommendation?: any[];
  categoryId?: string;
  lawyerId?: string;
  createdDate?: Date;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
}

export class LegalDocument implements ILegalDocument {
  constructor(
    public id?: string,
    public shortName?: string,
    public fullName?: string,
    public keywords?: string,
    public description?: string,
    public templatePreviewPath?: string,
    public templateFilePath?: string,
    public stepperConfigFilePath?: string,
    public workflowConfigFilePath?: string,
    public descriptionSections?: IDescriptionSection[],
    public price?: number,
    public autoFillConcernedEntities?: string[],
    public companiesAutoFillInputIdsByFieldName?: any[],
    public employersAutoFillInputIdsByFieldName?: any[],
    public documentsRecommendation?: any[],
    public categoryId?: string,
    public lawyerId?: string,
    public createdDate?: Date,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Date
  ) {
    this.createdDate = createdDate ? createdDate : null;
    this.lastModifiedBy = lastModifiedBy ? lastModifiedBy : null;
    this.lastModifiedDate = lastModifiedDate ? lastModifiedDate : null;
  }
}
