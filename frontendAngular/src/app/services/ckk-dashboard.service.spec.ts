import { TestBed } from '@angular/core/testing';

import { CkkDashboardService } from './ckk-dashboard.service';

describe('CkkDashboardService', () => {
  let service: CkkDashboardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CkkDashboardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
