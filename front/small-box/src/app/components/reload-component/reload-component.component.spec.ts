import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReloadComponentComponent } from './reload-component.component';

describe('ReloadComponentComponent', () => {
  let component: ReloadComponentComponent;
  let fixture: ComponentFixture<ReloadComponentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReloadComponentComponent]
    });
    fixture = TestBed.createComponent(ReloadComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
