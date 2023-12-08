import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule, FormControl} from '@angular/forms'
import { HttpClientModule } from '@angular/common/http';
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
import { CompletedSmallBoxComponent } from './components/completed-small-box/completed-small-box.component';
import { OpenContainerComponent } from './components/open-container/open-container.component'

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    SmallBoxComponent,
    NavbarComponent,
    ContainerComponent,
    CompletedSmallBoxComponent,
    OpenContainerComponent
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
     HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
