import {BrowserModule} from '@angular/platform-browser';
import {IconsModule, MDBBootstrapModule, TableModule} from 'angular-bootstrap-md';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ProjectsComponent} from './projects/projects.component';
import {TaskComponent} from './task/task.component';
import {MeetingComponent} from './meeting/meeting.component';
import {ProjectComponent} from './project/project.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {HomeComponent} from './home/home.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {UserComponent} from './user/user.component';
import {LoginComponent} from './login/login.component';
import {CookieService} from "ngx-cookie-service";
import {Tokeninterceptor} from "./services/tokeninterceptor";
import { LoaderComponent } from './loader/loader.component';
import { MeetingsComponent } from './meetings/meetings.component';
import {NgModule} from "@angular/core";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {MatDialogModule} from "@angular/material";


@NgModule({
  declarations: [
    AppComponent,
    ProjectsComponent,
    TaskComponent,
    MeetingComponent,
    ProjectComponent,
    HomeComponent,
    UserComponent,
    LoginComponent,
    LoaderComponent,
    MeetingsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    TableModule, IconsModule,
    FormsModule,
    ReactiveFormsModule,
    MatDialogModule,
    HttpClientModule,
    NgbModule,
    MDBBootstrapModule.forRoot()
  ],
  providers: [CookieService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: Tokeninterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
