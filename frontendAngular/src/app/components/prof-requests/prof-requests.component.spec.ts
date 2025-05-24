import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfRequestsComponent } from './prof-requests.component';

describe('ProfRequestsComponent', () => {
  let component: ProfRequestsComponent;
  let fixture: ComponentFixture<ProfRequestsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfRequestsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
