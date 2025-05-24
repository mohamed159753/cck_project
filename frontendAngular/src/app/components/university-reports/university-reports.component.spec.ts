import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UniversityReportsComponent } from './university-reports.component';

describe('UniversityReportsComponent', () => {
  let component: UniversityReportsComponent;
  let fixture: ComponentFixture<UniversityReportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UniversityReportsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UniversityReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
