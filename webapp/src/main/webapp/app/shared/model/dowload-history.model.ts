import { Moment } from 'moment';

export interface IDowloadHistory {
  id?: string;
  date?: Moment;
}

export class DowloadHistory implements IDowloadHistory {
  constructor(public id?: string, public date?: Moment) {}
}
