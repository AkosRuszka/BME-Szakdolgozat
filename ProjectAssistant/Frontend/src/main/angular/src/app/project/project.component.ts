  import { Component, OnInit } from '@angular/core';
  import {CommunicationService} from "../services/communication.service";
  import {Meeting, Project, Task} from "../models/dtos";
  import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.scss']
})
export class ProjectComponent implements OnInit {

  project: Project = null;
  tasks: Task[] = [];
  meetings: Meeting[] = [];
  newProject: Project = null;

  constructor(private service: CommunicationService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.service.getProject(this.route.snapshot.paramMap.get('projectName')).subscribe(
      (result: Project) => {
        this.project = result;
        this.service.getTasksForProject(this.project.name).subscribe(
          result => {
            this.tasks = result;
          }
        );
        this.service.getMeetingsFromProject(this.project.name).subscribe(
          result => {
            this.meetings = result;
          }
        )
      },
      error => {
        console.log("Hiba történt a projekt lekérdezésekor." + error);
      }
    )
  }

  deleteProject(name: string) {
    // TODO subscribe for error
    this.service.deleteProject(name);
  }

  addNewProject() {
    // TODO subscribe for error
    this.service.addNewProject(this.newProject);
  }

  updateProject(name: string) {
    // TODO subscribe for error
    this.service.updateProject(name, this.newProject);
  }

}
