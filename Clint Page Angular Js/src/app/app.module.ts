import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpModule } from '@angular/http';
import {  HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { RouterModule ,Routes  }from '@angular/router';
import { SocketIoModule, SocketIoConfig } from 'ng-socket-io';

import{ServHttpService}from './service/serv-http.service'

import { AppComponent } from './views/app.component';
import { HttpClientModule } from '@angular/common/http';
import { RegisterComponent } from './views/MngeUser/Register/register.component'
import { DashboardComponent } from './views/Dashboard/dashboard.component'
import { MembersComponent } from './views/Members/members.component'
import { UpdateComponent } from './views/MngeUser/Update/update.component';
import { DashboardControlComponent } from './views/dashboard-control/dashboard-control.component';
import { DashboardPirComponent } from './views/dashboard-pir/dashboard-pir.component';
import { CangeAdminComponent } from './views/MngeUser/CangeAdmin/cange-admin.component';
import { EditComponent } from './views/EditNameLed/edit.component';

//const config: SocketIoConfig = { url: 'http://localhost:3000', options: {} };
const config: SocketIoConfig = { url: 'http://'+ServHttpService.ip+':3000', options: {} };
const appRoutes=[
  {path:'dash' , component:DashboardControlComponent},
  {path:'',component:DashboardComponent},
  {path:'changadmin',component:CangeAdminComponent},
  {path:'Pirsensor',component:DashboardPirComponent},
  {path:'member',component:MembersComponent},
  {path:'reg',component:RegisterComponent},
  {path:'editnameled' ,component:EditComponent},
  {path:'update/:id/:GroupU/:user/:comment',component:UpdateComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    DashboardComponent,
    MembersComponent,
    UpdateComponent,
    DashboardControlComponent,
    DashboardPirComponent,
    CangeAdminComponent,
    EditComponent
  //  NvBar
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    HttpClientModule,
    SocketIoModule.forRoot(config),
    RouterModule.forRoot(appRoutes)
  ],
  providers: [ServHttpService],
  bootstrap: [AppComponent]
})
export class AppModule { 


}
