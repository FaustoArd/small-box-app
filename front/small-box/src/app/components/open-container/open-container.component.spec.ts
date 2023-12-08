import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenContainerComponent } from './open-container.component';

describe('OpenContainerComponent', () => {
  let component: OpenContainerComponent;
  let fixture: ComponentFixture<OpenContainerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OpenContainerComponent]
    });
    fixture = TestBed.createComponent(OpenContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
