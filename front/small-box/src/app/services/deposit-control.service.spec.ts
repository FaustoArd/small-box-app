import { TestBed } from '@angular/core/testing';

import { DepositControlService } from './deposit-control.service';

describe('DepositControlService', () => {
  let service: DepositControlService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DepositControlService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
