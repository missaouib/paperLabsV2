export interface ICategory {
  id?: string;
  name?: string;
  description?: string;
}

export class Category implements ICategory {
  constructor(public id?: string, public name?: string, public description?: string) {}
}
