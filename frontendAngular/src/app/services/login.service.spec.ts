import { TestBed } from '@angular/core/testing';

<<<<<<< HEAD
import { LoginService } from './auth.service';
=======
import { LoginService } from './login.service';
>>>>>>> db9f408 (Latest Changes)

describe('LoginService', () => {
  let service: LoginService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoginService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
