import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountsService } from '../services/accounts.service';
import { customer } from '../model/customer.model';

@Component({
  selector: 'app-customer-accounts',
  templateUrl: './customer-accounts.component.html',
  styleUrls: ['./customer-accounts.component.css'],
})
export class CustomerAccountsComponent implements OnInit {
  customerId!: number;
  customer!: customer;
  accounts: any[] = [];
  errorMessage!: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private accountService: AccountsService
  ) {
    this.customer = this.router.getCurrentNavigation()?.extras.state as customer;
  }

  ngOnInit(): void {
    this.customerId = +this.route.snapshot.params['id']; // convert string to number

    this.accountService.getAccountsByCustomer(this.customerId).subscribe({
      next: (data: any[]) => {
        this.accounts = data;
      },
      error: (err: any) => {
        this.errorMessage = err.message;
      },
    });
  }
}
