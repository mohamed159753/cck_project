import { TestBed } from '@angular/core/testing';

import { ProfRequestsService } from './prof-requests.service';

describe('ProfRequestsService', () => {
  let service: ProfRequestsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProfRequestsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
