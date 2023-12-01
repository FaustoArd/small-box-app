import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { ContainerComponent } from './components/container/container.component';
import { SmallBoxComponent } from './components/small-box/small-box.component';
import { CompletedSmallBoxComponent } from './components/completed-small-box/completed-small-box.component';

const routes: Routes = [
  { path:'home', component:HomeComponent },
  { path: '', component: HomeComponent, pathMatch: 'full' },
  { path:'container', component:ContainerComponent },
  { path:'small-box', component:SmallBoxComponent },
  { path:'completed',component:CompletedSmallBoxComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
