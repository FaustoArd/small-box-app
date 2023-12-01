import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompletedSmallBoxComponent } from './completed-small-box.component';

describe('CompletedSmallBoxComponent', () => {
  let component: CompletedSmallBoxComponent;
  let fixture: ComponentFixture<CompletedSmallBoxComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CompletedSmallBoxComponent]
    });
    fixture = TestBed.createComponent(CompletedSmallBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
