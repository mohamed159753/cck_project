import { TestBed } from '@angular/core/testing';

import { ProfMetricsService } from './prof-metrics.service';

describe('ProfMetricsService', () => {
  let service: ProfMetricsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProfMetricsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
