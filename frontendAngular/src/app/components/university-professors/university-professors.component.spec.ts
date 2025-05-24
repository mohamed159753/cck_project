import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UniversityProfessorsComponent } from './university-professors.component';

describe('UniversityProfessorsComponent', () => {
  let component: UniversityProfessorsComponent;
  let fixture: ComponentFixture<UniversityProfessorsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UniversityProfessorsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UniversityProfessorsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
