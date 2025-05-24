import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UniversitySidebarComponent } from './university-sidebar.component';

describe('UniversitySidebarComponent', () => {
  let component: UniversitySidebarComponent;
  let fixture: ComponentFixture<UniversitySidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UniversitySidebarComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UniversitySidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
