import { TestBed } from '@angular/core/testing';

import { DepositRequestService } from './deposit-request.service';

describe('DepositRequestService', () => {
  let service: DepositRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DepositRequestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
