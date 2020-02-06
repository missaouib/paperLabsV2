export interface IDocumentInfoStatistic {
  numberOfDownload?: number;
  numberOfCanceledDownload?: number;
  sumDocumentRevenue?: number;
}

export class DocumentInfoStatistic implements IDocumentInfoStatistic {
  constructor(public numberOfDownload?: number, public numberOfCanceledDownload?: number, public sumDocumentRevenue?: number) {}
}
