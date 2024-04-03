import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { ContainerComponent } from './components/container/container.component';
import { SmallBoxComponent } from './components/small-box/small-box.component';
import { CompletedSmallBoxComponent } from './components/completed-small-box/completed-small-box.component';
import { OpenContainerComponent } from './components/open-container/open-container.component';
import { PresentationComponent } from './components/presentation/presentation.component';
import { LoginComponent } from './components/authorization/login/login.component';
import { RegistrationComponent } from './components/authorization/registration/registration.component';
import { OrganizationComponent } from './components/authorization/organization/organization.component';
import { OrganizationSetupComponent } from './components/organization-setup/organization-setup.component';
import { MemoSingleComponent } from './components/work-template/memo-single/memo-single.component';
import { MemoSingleShowComponent } from './components/show-work-template/memo-single-show/memo-single-show.component';
import { WorkTemplateListComponent } from './components/work-template/work-template-list/work-template-list.component';
import { ManualReferComponent } from './components/work-template/manual-refer/manual-refer.component';
import { MemoSingleEditComponent } from './components/work-template/memo-single-edit/memo-single-edit.component';
import { DispatchMainComponent } from './components/dispatch/dispatch-main/dispatch-main.component';
import { DispatchMatComponent } from './components/dispatch/dispatch-mat/dispatch-mat.component';
import { DepositHomeComponent } from './components/deposit-control/deposit-home/deposit-home.component';
import { UserPanelComponent } from './components/user-panel/user-panel.component';

const routes: Routes = [
  { path:'home', component:HomeComponent },
  { path: '', component: HomeComponent, pathMatch: 'full' },
  { path:'container', component:ContainerComponent },
  { path:'small-box', component:SmallBoxComponent },
  { path:'completed',component:CompletedSmallBoxComponent },
  { path:'completed-by-id',component:CompletedSmallBoxComponent },
  { path:'open-container',component:OpenContainerComponent },
  { path:'presentation', component:PresentationComponent },
  { path:'login', component:LoginComponent },
  { path:'register', component:RegistrationComponent},
  { path:'org', component:OrganizationComponent},
  { path:'org-setup', component:OrganizationSetupComponent},
  { path:'memo-create', component:MemoSingleComponent},
  { path:'memo-edit/:id',component:MemoSingleEditComponent},
  { path:'memo-show', component:MemoSingleShowComponent},
  { path:'work-template-list', component:WorkTemplateListComponent},
  { path:'refer-create',component:ManualReferComponent},
  { path:'dispatch',component:DispatchMainComponent},
  { path:'dispatch-page',component:DispatchMatComponent},
  { path:'deposit-home', component:DepositHomeComponent},
  { path:'user-panel',component:UserPanelComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'reload'})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
