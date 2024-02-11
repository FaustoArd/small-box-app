import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManualReferComponent } from './manual-refer.component';

describe('ManualReferComponent', () => {
  let component: ManualReferComponent;
  let fixture: ComponentFixture<ManualReferComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManualReferComponent]
    });
    fixture = TestBed.createComponent(ManualReferComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
