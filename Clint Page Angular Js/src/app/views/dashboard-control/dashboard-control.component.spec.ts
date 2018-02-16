import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardControlComponent } from './dashboard-control.component';

describe('DashboardControlComponent', () => {
  let component: DashboardControlComponent;
  let fixture: ComponentFixture<DashboardControlComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DashboardControlComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardControlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
