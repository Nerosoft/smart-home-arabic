import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardPirComponent } from './dashboard-pir.component';

describe('DashboardPirComponent', () => {
  let component: DashboardPirComponent;
  let fixture: ComponentFixture<DashboardPirComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DashboardPirComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardPirComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
