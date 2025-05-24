import { TestBed } from '@angular/core/testing';

import { CckManageReservationsServiceService } from './cck-manage-reservations-service.service';

describe('CckManageReservationsServiceService', () => {
  let service: CckManageReservationsServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CckManageReservationsServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
