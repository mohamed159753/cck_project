import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BillsManagementComponent } from './bills-management.component';

describe('BillsManagementComponent', () => {
  let component: BillsManagementComponent;
  let fixture: ComponentFixture<BillsManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BillsManagementComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BillsManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
