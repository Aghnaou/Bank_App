import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { customer } from '../model/customer.model';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  backendHost: string = 'http://localhost:8085';
  constructor(private http: HttpClient) {}

  public getCustomers(): Observable<Array<customer>> {
    return this.http.get<Array<customer>>(this.backendHost + '/customers');
  }

  public searchCustomers(keyword: string): Observable<Array<customer>> {
    return this.http.get<Array<customer>>(
      this.backendHost + '/customers/search?keyword=' + keyword
    );
  }

  public SaveCustomer(customer: customer): Observable<customer> {
    return this.http.post<customer>(this.backendHost + '/customers', customer);
  }

  public DeleteCustomer(id: number) {
    return this.http.delete(this.backendHost + '/customers/' + id);
  }
}
