import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EcsListComponent } from './ecs-list.component';

describe('EcsListComponent', () => {
  let component: EcsListComponent;
  let fixture: ComponentFixture<EcsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EcsListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EcsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
