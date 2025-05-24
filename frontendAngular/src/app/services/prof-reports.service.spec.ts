import { TestBed } from '@angular/core/testing';

import { ProfReportsService } from './prof-reports.service';

describe('ProfReportsService', () => {
  let service: ProfReportsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProfReportsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
