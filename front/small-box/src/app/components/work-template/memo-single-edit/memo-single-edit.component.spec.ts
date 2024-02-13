import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MemoSingleEditComponent } from './memo-single-edit.component';

describe('MemoSingleEditComponent', () => {
  let component: MemoSingleEditComponent;
  let fixture: ComponentFixture<MemoSingleEditComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MemoSingleEditComponent]
    });
    fixture = TestBed.createComponent(MemoSingleEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
