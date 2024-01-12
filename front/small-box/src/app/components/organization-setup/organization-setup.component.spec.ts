import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrganizationSetupComponent } from './organization-setup.component';

describe('OrganizationSetupComponent', () => {
  let component: OrganizationSetupComponent;
  let fixture: ComponentFixture<OrganizationSetupComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrganizationSetupComponent]
    });
    fixture = TestBed.createComponent(OrganizationSetupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
