import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CangeAdminComponent } from './cange-admin.component';

describe('CangeAdminComponent', () => {
  let component: CangeAdminComponent;
  let fixture: ComponentFixture<CangeAdminComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CangeAdminComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CangeAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
