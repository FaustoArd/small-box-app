import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DispatchMatComponent } from './dispatch-mat.component';

describe('DispatchMatComponent', () => {
  let component: DispatchMatComponent;
  let fixture: ComponentFixture<DispatchMatComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DispatchMatComponent]
    });
    fixture = TestBed.createComponent(DispatchMatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
