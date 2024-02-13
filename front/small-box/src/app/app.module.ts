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
    MemoSingleEditComponent
    
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
    
    
  ],
 
  providers: [CookieService,
  {
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi:true
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
