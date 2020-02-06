import { Moment } from 'moment';

export interface IEmployee {
  id?: string;
  employeeFullName?: string;
  employeeCinNumber?: string;
  employeeCinDeliveredDate?: Moment;
  employeeCinDeliveredLocation?: string;
  employeeFullAddress?: string;
  employeePostion?: string;
}

export class Employee implements IEmployee {
  constructor(
    public id?: string,
    public employeeFullName?: string,
    public employeeCinNumber?: string,
    public employeeCinDeliveredDate?: Moment,
    public employeeCinDeliveredLocation?: string,
    public employeeFullAddress?: string,
    public employeePostion?: string
  ) {}
}
