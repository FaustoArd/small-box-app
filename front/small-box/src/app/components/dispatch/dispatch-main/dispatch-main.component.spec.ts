import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DispatchMainComponent } from './dispatch-main.component';

describe('DispatchMainComponent', () => {
  let component: DispatchMainComponent;
  let fixture: ComponentFixture<DispatchMainComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DispatchMainComponent]
    });
    fixture = TestBed.createComponent(DispatchMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
