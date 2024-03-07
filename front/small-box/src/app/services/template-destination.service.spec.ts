import { TestBed } from '@angular/core/testing';

import { TemplateDestinationService } from './template-destination.service';

describe('TemplateDestinationService', () => {
  let service: TemplateDestinationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TemplateDestinationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
