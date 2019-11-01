import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ProjectComponent} from "./project/project.component";
import {TaskComponent} from "./task/task.component";
import {MeetingComponent} from "./meeting/meeting.component";
import {ProjectsComponent} from "./projects/projects.component";
import {HomeComponent} from "./home/home.component";
import {UserComponent} from "./user/user.component";
import {LoginComponent} from "./login/login.component";
import {MeetingsComponent} from "./meetings/meetings.component";

const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: 'home',
    component: HomeComponent
  },
  {
    path: 'user',
    component: UserComponent
  },
  {
    path: 'projects',
    component: ProjectsComponent
  },
  {
    path: 'project/:projectName',
    component: ProjectComponent
  },
  {
    path: "task/:taskName",
    component: TaskComponent
  },
  {
    path: "meeting/:meetingName",
    component: MeetingComponent
  },
  {
    path: "meetings",
    component: MeetingsComponent
  },
  {
    path: "login",
    component: LoginComponent
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
