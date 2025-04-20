import { TestBed } from '@angular/core/testing';

import { UniversityReportsServiceService } from './university-reports-service.service';

describe('UniversityReportsServiceService', () => {
  let service: UniversityReportsServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UniversityReportsServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
