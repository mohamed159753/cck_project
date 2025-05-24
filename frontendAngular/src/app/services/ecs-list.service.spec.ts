import { TestBed } from '@angular/core/testing';

import { EcsListService } from './ecs-list.service';

describe('EcsListService', () => {
  let service: EcsListService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EcsListService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
