import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AccountsService } from '../services/accounts.service';
import { catchError, Observable, throwError } from 'rxjs';
import { AccountDetails } from '../model/account.model';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrl: './accounts.component.css',
})
export class AccountsComponent implements OnInit {
  accountFormGroup!: FormGroup;
  currentPage: number = 0;
  pageSize: number = 5;
  account!: Observable<AccountDetails>;
  operationsFormGroup!: FormGroup;
  errorMessage!: string;

  constructor(
    private fb: FormBuilder,
    private accountService: AccountsService
  ) {}

  ngOnInit(): void {
    this.accountFormGroup = this.fb.group({
      accountId: this.fb.control(''),
    });
    this.operationsFormGroup = this.fb.group({
      operationType: this.fb.control(null),
      amount: this.fb.control(0),
      description: this.fb.control(null),
      accountDestination: this.fb.control(null),
    });
  }

  handlesearchAccount() {
    let accountId: string = this.accountFormGroup.value.accountId;
    this.account = this.accountService
      .getAccount(accountId, this.currentPage, this.pageSize)
      .pipe(
        catchError((err) => {
          this.errorMessage = err.message;
          return throwError(() => new Error(err.message));
        })
      );
  }

  gotoPage(page: number) {
    this.currentPage = page;
    this.handlesearchAccount();
  }

  handleAccountOperation() {
    console.log('Operation button clicked!!!!');
    let accountId: string = this.accountFormGroup.value.accountId;
    let operationType = this.operationsFormGroup.value.operationType;
    let amount: number = this.operationsFormGroup.value.amount;
    let description: string = this.operationsFormGroup.value.description;
    let accountDestination: string =
      this.operationsFormGroup.value.accountDestination;
    if (operationType == 'DEBIT') {
      this.accountService.debit(accountId, amount, description).subscribe({
        next: (data) => {
          alert('Success credit');
          this.handlesearchAccount();
        },
        error: (err) => {
          console.log(err);
        },
      });
    } else if (operationType == 'CREDIT') {
      this.accountService.credit(accountId, amount, description).subscribe({
        next: (data) => {
          alert('Success debit');
          this.handlesearchAccount();
        },
        error: (err) => {
          console.log(err);
        },
      });
    } else if (operationType == 'TRANSFER') {
      this.accountService
        .transfer(accountId, accountDestination, amount)
        .subscribe({
          next: (data) => {
            alert('Success transfer');
            this.handlesearchAccount();
          },
          error: (err) => {
            console.log(err);
          },
        });
    }
    this.operationsFormGroup.reset();
  }
}
