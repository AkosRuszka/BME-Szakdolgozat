import {Component, OnInit} from '@angular/core';
import {CommunicationService} from "../services/communication.service";
import {Meeting, Project, User} from "../models/dtos";
import {TokenService} from "../services/token.service";
import {ActivatedRoute} from "@angular/router";
import {AppComponent} from "../app.component";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  projects: Project[] = [];
  meetings: Meeting[] = [];
  user: User = null;
  loaded = false;

  constructor(private communication: CommunicationService, private tokenService: TokenService,
              private route: ActivatedRoute,
              private tokenStore: TokenService) {
  }

  ngOnInit() {
    this.communication.getProjects(5, 0).subscribe(
      result => {
        this.projects = result;
      },
      error => {
        console.log("Hiba történt a projektek lekérdezésénél", error);
      },
    );
    this.communication.getMeetings(5, 0).subscribe(
      result => {
        this.meetings = result;

      },
      error => {
        console.log("Hiba történt a meetingek lekérdezésénél", error);
      }
    );
    this.communication.getUserInfo().subscribe(
      result => {
        this.user = result;
        this.loaded = true;
      },
    error => {
      console.log("Hiba a felhasználó lekérezésénél", error)
    });
  }
}
