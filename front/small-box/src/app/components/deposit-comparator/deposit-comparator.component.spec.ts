import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DepositComparatorComponent } from './deposit-comparator.component';

describe('DepositComparatorComponent', () => {
  let component: DepositComparatorComponent;
  let fixture: ComponentFixture<DepositComparatorComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DepositComparatorComponent]
    });
    fixture = TestBed.createComponent(DepositComparatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
