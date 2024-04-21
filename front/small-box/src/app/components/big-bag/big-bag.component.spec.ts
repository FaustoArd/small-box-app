import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BigBagComponent } from './big-bag.component';

describe('BigBagComponent', () => {
  let component: BigBagComponent;
  let fixture: ComponentFixture<BigBagComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BigBagComponent]
    });
    fixture = TestBed.createComponent(BigBagComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
