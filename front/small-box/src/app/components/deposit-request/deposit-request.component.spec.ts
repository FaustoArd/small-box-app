import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DepositRequestComponent } from './deposit-request.component';

describe('DepositRequestComponent', () => {
  let component: DepositRequestComponent;
  let fixture: ComponentFixture<DepositRequestComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DepositRequestComponent]
    });
    fixture = TestBed.createComponent(DepositRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
