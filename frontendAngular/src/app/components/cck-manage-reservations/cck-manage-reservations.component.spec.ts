import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CckManageReservationsComponent } from './cck-manage-reservations.component';

describe('CckManageReservationsComponent', () => {
  let component: CckManageReservationsComponent;
  let fixture: ComponentFixture<CckManageReservationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CckManageReservationsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CckManageReservationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
