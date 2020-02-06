export interface IStatistics {
  userStatistics?: any;
  orderStatistics?: any;
}

export class Statistics implements IStatistics {
  constructor(public userStatistics?: any, public orderStatistics?: any) {}
}
