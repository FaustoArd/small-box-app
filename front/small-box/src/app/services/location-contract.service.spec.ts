import { TestBed } from '@angular/core/testing';

import { LocationContractService } from './location-contract.service';

describe('LocationContractService', () => {
  let service: LocationContractService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LocationContractService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
