import { TestBed } from '@angular/core/testing';

import { SmallBoxService } from './small-box.service';

describe('SmallBoxService', () => {
  let service: SmallBoxService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SmallBoxService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
