import { TestBed } from '@angular/core/testing';

import { UniversityManageReservationsService } from './university-manage-reservations.service';

describe('UniversityManageReservationsService', () => {
  let service: UniversityManageReservationsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UniversityManageReservationsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
