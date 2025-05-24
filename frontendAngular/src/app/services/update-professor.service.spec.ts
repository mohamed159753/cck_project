import { TestBed } from '@angular/core/testing';

import { UpdateProfessorService } from './update-professor.service';

describe('UpdateProfessorService', () => {
  let service: UpdateProfessorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UpdateProfessorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
