import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CommunicationService} from "../services/communication.service";
import {Project} from "../models/dtos";

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.scss']
})
export class ProjectsComponent implements OnInit {
  projects: Project[] = [];

  pageNumber = 0;

  selectedProject: Project = null;
  newProjectForm: FormGroup;

  constructor(private fb: FormBuilder, private communication: CommunicationService) {
    this.newProjectForm = fb.group({
      projectName: ['', Validators.required],
      projectOwner: ['', [Validators.email, Validators.required]]
    });
  }

  ngOnInit() {
    this.communication.getProjects(this.pageNumber, 0).subscribe(
      result => {
        this.projects = result;
      },
      error => {
        console.log("Hiba a projektek lekérdezésekkor", error);
      }
    );
  }

  newProject() {
    console.log(this.newProjectForm.controls.projectName.value);
    return;
  }

  showProjectDetails(projectName: string) {
    this.communication.getProject(projectName).subscribe(
      result => {
        this.selectedProject = result;
      },
      error1 => {
        console.log("Hiba egy konkrét projekt lekérdezésekor", error1);
      }
    )
  }
}
