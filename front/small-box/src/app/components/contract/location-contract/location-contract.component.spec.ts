import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LocationContractComponent } from './location-contract.component';

describe('LocationContractComponent', () => {
  let component: LocationContractComponent;
  let fixture: ComponentFixture<LocationContractComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LocationContractComponent]
    });
    fixture = TestBed.createComponent(LocationContractComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
