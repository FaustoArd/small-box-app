import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { ContainerComponent } from './components/container/container.component';
import { SmallBoxComponent } from './components/small-box/small-box.component';
import { CompletedSmallBoxComponent } from './components/completed-small-box/completed-small-box.component';
import { OpenContainerComponent } from './components/open-container/open-container.component';
import { PresentationComponent } from './components/presentation/presentation.component';
import { LoginComponent } from './components/authorization/login/login.component';

const routes: Routes = [
  { path:'home', component:HomeComponent },
  { path: '', component: HomeComponent, pathMatch: 'full' },
  { path:'container', component:ContainerComponent },
  { path:'small-box', component:SmallBoxComponent },
  { path:'completed',component:CompletedSmallBoxComponent},
  { path:'completed-by-id/:id',component:CompletedSmallBoxComponent},
  { path:'open-container',component:OpenContainerComponent},
  { path:'presentation', component:PresentationComponent},
  { path:'login', component:LoginComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
