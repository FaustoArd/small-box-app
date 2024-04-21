import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms'
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HomeComponent } from './components/home/home.component';
import { SmallBoxComponent } from './components/small-box/small-box.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { ContainerComponent } from './components/container/container.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatMenuModule} from '@angular/material/menu';
import {MatButtonModule} from '@angular/material/button';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import { MatToolbarModule} from '@angular/material/toolbar';
import { MatDialogModule} from '@angular/material/dialog'
import { CompletedSmallBoxComponent } from './components/completed-small-box/completed-small-box.component';
import { OpenContainerComponent } from './components/open-container/open-container.component';
import { DialogTemplateComponent } from './components/dialog/dialog-template/dialog-template.component';
import { PresentationComponent } from './components/presentation/presentation.component'
import { NgxCaptureModule } from 'ngx-capture';
import {CookieService} from 'ngx-cookie-service';
import { LoginComponent } from './components/authorization/login/login.component';
import { RegistrationComponent } from './components/authorization/registration/registration.component';
import { AuthInterceptor } from './components/interceptor/auth-interceptor';
import { OrganizationComponent } from './components/authorization/organization/organization.component';
import { OrganizationSetupComponent } from './components/organization-setup/organization-setup.component';
import { MemoSingleComponent } from './components/work-template/memo-single/memo-single.component';
import { MemoSingleShowComponent } from './components/show-work-template/memo-single-show/memo-single-show.component';
import { WorkTemplateListComponent } from './components/work-template/work-template-list/work-template-list.component';
import { ManualReferComponent } from './components/work-template/manual-refer/manual-refer.component';
import { MemoSingleEditComponent } from './components/work-template/memo-single-edit/memo-single-edit.component';
import { LocationContractComponent } from './components/contract/location-contract/location-contract.component';
import { DispatchMainComponent } from './components/dispatch/dispatch-main/dispatch-main.component';
import { ConfirmDialogComponent } from './components/confirm-dialog/confirm-dialog/confirm-dialog.component';
import { DispatchMatComponent } from './components/dispatch/dispatch-mat/dispatch-mat.component';
import {NgxPaginationModule} from 'ngx-pagination';
import { AdminPanelComponent } from './components/admin-panel/admin-panel/admin-panel.component';
import { DepositHomeComponent } from './components/deposit-control/deposit-home/deposit-home.component';
import { UserPanelComponent } from './components/user-panel/user-panel.component';
import { RouterModule } from '@angular/router';
import { BigBagComponent } from './components/big-bag/big-bag.component';






@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    SmallBoxComponent,
    NavbarComponent,
    ContainerComponent,
    CompletedSmallBoxComponent,
    OpenContainerComponent,
    DialogTemplateComponent,
    PresentationComponent,
    LoginComponent,
    RegistrationComponent,
    OrganizationComponent,
    OrganizationSetupComponent,
    MemoSingleComponent,
    MemoSingleShowComponent,
    WorkTemplateListComponent,
    ManualReferComponent,
    MemoSingleEditComponent,
    LocationContractComponent,
    DispatchMainComponent,
    ConfirmDialogComponent,
    DispatchMatComponent,
    AdminPanelComponent,
    DepositHomeComponent,
    UserPanelComponent,
    BigBagComponent,
   
   
    
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule, 
    ReactiveFormsModule,
     BrowserAnimationsModule,
     MatMenuModule,
     MatButtonModule,
     MatSnackBarModule,
     MatToolbarModule,
     HttpClientModule,
     MatDialogModule,
     NgxCaptureModule,
     NgxPaginationModule,
   
    
    
  ],
 
 
  providers: [CookieService,
    
  {
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi:true,
    
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
