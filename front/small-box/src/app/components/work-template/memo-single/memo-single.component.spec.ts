import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MemoSingleComponent } from './memo-single.component';

describe('MemoSingleComponent', () => {
  let component: MemoSingleComponent;
  let fixture: ComponentFixture<MemoSingleComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MemoSingleComponent]
    });
    fixture = TestBed.createComponent(MemoSingleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
