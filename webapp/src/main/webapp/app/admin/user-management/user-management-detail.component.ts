import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { User } from 'app/core/user/user.model';
import { UserManagementService } from 'app/admin/user-management/user-management-service';

@Component({
  selector: 'jhi-user-mgmt-detail',
  templateUrl: './user-management-detail.component.html'
})
export class UserMgmtDetailComponent implements OnInit {
  user: User;
  numberPaidOrder = 0;
  sumAllPaidOrder = 0;
  numberAbandonedOrder = 0;

  constructor(private route: ActivatedRoute, private userManagementService: UserManagementService) {}

  ngOnInit() {
    this.route.data.subscribe(({ user }) => {
      this.user = user.body ? user.body : user;
      this.getNumberOfPaidOrders(user.body.login);
      this.getSumAllPaidOrders(user.body.login);
      this.getNumberAbandonnedOrder(user.body.login);
    });
  }

  getNumberOfPaidOrders(userLogin: number) {
    this.userManagementService.getUserStatistics(userLogin).subscribe(res => {
      this.numberPaidOrder = res.body.numberPaidOrder;
    });
  }

  getSumAllPaidOrders(userLogin: number) {
    this.userManagementService.getUserStatistics(userLogin).subscribe(res => {
      this.sumAllPaidOrder = res.body.sumAllPaidOrder;
    });
  }

  getNumberAbandonnedOrder(userLogin: number) {
    this.userManagementService.getUserStatistics(userLogin).subscribe(res => {
      this.numberAbandonedOrder = res.body.numberAbandonedOrder;
    });
  }
}
