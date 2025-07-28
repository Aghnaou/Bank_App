import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { customer } from '../model/customer.model';

@Component({
  selector: 'app-customer-accounts',
  templateUrl: './customer-accounts.component.html',
  styleUrl: './customer-accounts.component.css',
})
export class CustomerAccountsComponent implements OnInit {
  customerId!: string;
  customer!: customer;
  constructor(private route: ActivatedRoute, private router: Router) {
    this.customer = this.router.getCurrentNavigation()?.extras
      .state as customer;
  }

  ngOnInit(): void {
    this.customerId = this.route.snapshot.params['id'];
  }
}
