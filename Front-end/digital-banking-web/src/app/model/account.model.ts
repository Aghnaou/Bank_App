export interface AccountDetails {
  id: string;
  balance: number;
  currentPage: number;
  totalPages: number;
  accountOperationDTOS: AccountOperation[];
  pagesize: number;
}

export interface AccountOperation {
  id: number;
  operationDate: Date;
  amount: number;
  type: string;
  description: string;
}
