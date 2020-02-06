export interface IUserInfoStatistic {
  numberPaidOrder?: number;
  sumAllPaidOrder?: number;
  numberAbandonedOrder?: number;
}

export class UserInfoStatistic implements IUserInfoStatistic {
  constructor(public numberPaidOrder?: number, public sumAllPaidOrder?: number, public numberAbandonedOrder?: number) {}
}
