import { Component, OnInit } from '@angular/core';
import {CommunicationService} from "../services/communication.service";
import {Meeting, User} from "../models/dtos";

@Component({
  selector: 'app-meetings',
  templateUrl: './meetings.component.html',
  styleUrls: ['./meetings.component.scss']
})
export class MeetingsComponent implements OnInit {

  headElements = ['Meeting name', 'Project', 'Date', 'Location'];
  meetings: Meeting[];
  users: User[];
  pageNumber = 0;

  constructor(private httpService: CommunicationService) { }

  ngOnInit() {
    this.httpService.getMeetings(0, 7).subscribe(
      data => {
        this.meetings = data;
      },
      error1 => {
        console.log("Hiba a meetingek lekérdezése során");
      }
    )
  }

  getUsers(meeting: Meeting) {

  }

}
