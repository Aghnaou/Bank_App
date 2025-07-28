import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../services/customer.service';
import { catchError, map, Observable, throwError } from 'rxjs';
import { customer } from '../model/customer.model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css',
})
export class CustomersComponent implements OnInit {
  customers!: Observable<Array<customer>>;
  errorMessage!: String;
  searchFormGroup!: FormGroup;

  constructor(
    private customerService: CustomerService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  // ngOnInit(): void {
  //   this.http.get('http://localhost:8085/customers').subscribe(
  //     (data) => {
  //       this.customers = data;
  //     },
  //     (error) => {
  //       console.log(error);
  //     }
  //   );
  // }
  ngOnInit(): void {
    this.searchFormGroup = this.fb.group({
      keyword: this.fb.control(''),
    });
    this.customers = this.customerService.getCustomers().pipe(
      catchError((err) => {
        this.errorMessage = err.message;
        return throwError(() => err);
      })
    );
  }

  handleSearchCustomers() {
    let kw = this.searchFormGroup?.value.keyword;
    this.customers = this.customerService.searchCustomers(kw).pipe(
      catchError((err) => {
        this.errorMessage = err.message;
        return throwError(() => err);
      })
    );
  }

  handleDeleteCustomer(c: customer) {
    let conf = confirm('Are you sure you want to delete this customer');

    if (!conf) return;

    this.customerService.DeleteCustomer(c.id).subscribe({
      next: (res) => {
        // Way number 1 :
        // this.handleSearchCustomers();
        // way number 2 :
        this.customers = this.customers.pipe(
          map((data) => {
            let index = data.indexOf(c);
            data.slice(index, 1);
            return data;
          })
        );
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  handleCustomerAccounts(c: customer) {
    this.router.navigateByUrl('/customer-accounts/' + c.id, { state: c });
  }
}
