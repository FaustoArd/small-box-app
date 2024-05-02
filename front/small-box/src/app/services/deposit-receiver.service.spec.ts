import { TestBed } from '@angular/core/testing';

import { DepositReceiverService } from './deposit-receiver.service';

describe('DepositReceiverService', () => {
  let service: DepositReceiverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DepositReceiverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
