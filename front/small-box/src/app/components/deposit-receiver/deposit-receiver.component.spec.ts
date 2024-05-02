import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DepositReceiverComponent } from './deposit-receiver.component';

describe('DepositReceiverComponent', () => {
  let component: DepositReceiverComponent;
  let fixture: ComponentFixture<DepositReceiverComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DepositReceiverComponent]
    });
    fixture = TestBed.createComponent(DepositReceiverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
