import { TestBed } from '@angular/core/testing';

import { WorkTemplateService } from './work-template.service';

describe('WorkTemplateService', () => {
  let service: WorkTemplateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorkTemplateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
