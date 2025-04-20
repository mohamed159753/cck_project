import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UniversityBillingComponent } from './university-billing.component';

describe('UniversityBillingComponent', () => {
  let component: UniversityBillingComponent;
  let fixture: ComponentFixture<UniversityBillingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UniversityBillingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UniversityBillingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
