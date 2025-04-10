import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfRevComponent } from './prof-rev.component';

describe('ProfRevComponent', () => {
  let component: ProfRevComponent;
  let fixture: ComponentFixture<ProfRevComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfRevComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfRevComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
