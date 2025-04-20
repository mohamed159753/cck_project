import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UniversityBillingDetailsComponent } from './university-billing-details.component';

describe('UniversityBillingDetailsComponent', () => {
  let component: UniversityBillingDetailsComponent;
  let fixture: ComponentFixture<UniversityBillingDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UniversityBillingDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UniversityBillingDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
