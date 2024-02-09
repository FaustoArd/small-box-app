import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MemoSingleShowComponent } from './memo-single-show.component';

describe('MemoSingleShowComponent', () => {
  let component: MemoSingleShowComponent;
  let fixture: ComponentFixture<MemoSingleShowComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MemoSingleShowComponent]
    });
    fixture = TestBed.createComponent(MemoSingleShowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
