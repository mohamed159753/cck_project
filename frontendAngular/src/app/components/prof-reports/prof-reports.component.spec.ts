import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfReportsComponent } from './prof-reports.component';

describe('ProfReportsComponent', () => {
  let component: ProfReportsComponent;
  let fixture: ComponentFixture<ProfReportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfReportsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
