export interface IDescriptionSection {
  id?: string;
  title?: string;
  content?: string;
}

export class DescriptionSection implements IDescriptionSection {
  constructor(public id?: string, public title?: string, public content?: string) {}
}
