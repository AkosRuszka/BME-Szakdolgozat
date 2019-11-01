import {Component, Input, OnInit} from '@angular/core';
import {Meeting, Minutes, TaskDescription} from "../models/dtos";
import {CommunicationService} from "../services/communication.service";
import {ActivatedRoute} from "@angular/router";
import {Form, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-meeting',
  templateUrl: './meeting.component.html',
  styleUrls: ['./meeting.component.scss'],
  providers: [DatePipe]
})
export class MeetingComponent implements OnInit {

  @Input()
  edit = false;
  @Input()
  editable = true;
  @Input()
  meeting: Meeting;
  minute: Minutes;
  editMeetingForm: FormGroup;
  addMinuteForm: FormGroup;
  addTaskForm: FormGroup;
  meetingNameIsAlreadyExist = false;
  userNotExist = false;
  newAttendeeEmailList = [];
  newAbsentEmailSet = [];
  newTaskSet : TaskDescription[];

  constructor(private fb: FormBuilder, private httpService: CommunicationService, private route: ActivatedRoute,
              public datepipe: DatePipe) {
    this.addMinuteForm = this.fb.group({
      minuteTitle : ['', Validators.required],
      absentEmailSet: ['', Validators.email]
    });
    this.addTaskForm = this.fb.group({
      taskName : ['', Validators.required],
      taskDescription : ['', Validators.required],
      estimatingDate : ['', Validators.required]
    });
    this.newTaskSet = [];
  }

  ngOnInit() {
    if (this.meeting == undefined) {
      this.httpService.getMeeting(this.route.snapshot.paramMap.get('meetingName')).subscribe(
        result => {
          this.meeting = result;
          this.newAttendeeEmailList = result.attendeeEmailSet.slice();
          this.editMeetingForm = this.fb.group({
            meetingName: [this.meeting.name, Validators.required],
            projectName: [this.meeting.projectName, Validators.required],
            meetingDate: [this.meeting.date, Validators.required],
            meetingStart: [this.meeting.startTime, Validators.required],
            meetingEnd: [this.meeting.endTime, Validators.required],
            meetingLocation: [this.meeting.location, Validators.required],
            meetingDescription: [this.meeting.description, Validators.required],
            attendeeEmail: ['', Validators.email],
          },);
          if(this.meeting.minuteName != undefined) {
            this.httpService.getMinute(this.meeting.name).subscribe(
              result => {
                this.minute = result;
                this.newAbsentEmailSet = this.minute.absentEmailSet.slice();
              },
              error1 => console.log("Nem sikerült lekérdezni a meetinget a Minute betöltéséhez.")
            );
          }
        },
        error1 =>
          console.log("Hiba történt a meeting lekérdezésekor")
      );
    }
  }

  cancelEdit() {
    this.edit = false;
    this.newAttendeeEmailList = this.meeting.attendeeEmailSet.slice();
    this.editMeetingForm.value['attendeeEmail'] = '';
  }

  cancelAddMinute() {
    if(this.minute != undefined) {
      this.newAbsentEmailSet = this.minute.absentEmailSet.slice()
    } else {
      this.newAbsentEmailSet = [];
    }
  }

  deleteMeeting() {
    console.log("A törlés ki van kapcsolva");
    // this.httpService.deleteMeeting(this.meeting.name);
  }

  editMeeting() {
    if (this.editMeetingForm.get('meetingName').value != this.meeting.name) {
      this.httpService.checkMeetingName(this.editMeetingForm.get("meetingName").value).subscribe(
        result => {
          if (result) {
            this.meetingNameIsAlreadyExist = false;
            this.saveMeeting();
          } else {
            this.meetingNameIsAlreadyExist = true;
          }
        })
    } else {
      this.saveMeeting();
    }
  }

  addAttendee(event: any) {
    this.httpService.existUser(event.target.value).subscribe(
      result => {
        if (result) {
          if(this.newAttendeeEmailList.indexOf(event.target.value) == -1) {
            this.newAttendeeEmailList.push(event.target.value);
          }
          this.userNotExist = false;
          this.editMeetingForm.value['attendeeEmail'] = '';
        } else {
          this.userNotExist = true;
        }
      }
    )
  }

  addAbsentEmail(event: any) {
    this.httpService.existUser(event.target.value).subscribe(
      result => {
        if (result) {
          if(this.newAbsentEmailSet.indexOf(event.target.value) == -1) {
            this.newAbsentEmailSet.push(event.target.value);
          }
          this.userNotExist = false;
        } else {
          this.userNotExist = true;
        }
      }
    )
  }

  addTask() {
    // TODO ellenorizni kell hogy ne szerepeljen ugyan ilyen nevvel task
    var task = new TaskDescription();
    task.name = this.addTaskForm.get('taskName').value;
    task.description = this.addTaskForm.get('taskDescription').value;
    task.estimatingDate = this.addTaskForm.get('estimatingDate').value;
    if(this.newTaskSet.indexOf(task) == -1) {
      this.newTaskSet.push(task);
    }
  }

  removeTask(task: TaskDescription) {
    const index = this.newTaskSet.indexOf(task, 0);
    if (index > -1) {
      this.newTaskSet.splice(index, 1);
    }
  }

  remove(attendees: string) {
    const index = this.newAttendeeEmailList.indexOf(attendees, 0);
    if (index > -1) {
      this.newAttendeeEmailList.splice(index, 1);
    }
  }

  removeAbsent(attendees: string) {
    const index = this.newAbsentEmailSet.indexOf(attendees, 0);
    if (index > -1) {
      this.newAbsentEmailSet.splice(index, 1);
    }
  }

  saveMeeting() {
    let oldName = this.meeting.name;
    this.meeting.name = this.editMeetingForm.get('meetingName').value;
    this.meeting.date = this.datepipe.transform(this.editMeetingForm.get('meetingDate').value, 'yyyy-MM-dd');
    this.meeting.startTime = this.editMeetingForm.get('meetingStart').value;
    this.meeting.endTime = this.editMeetingForm.get('meetingEnd').value;
    this.meeting.description = this.editMeetingForm.get('meetingDescription').value;
    this.meeting.location = this.editMeetingForm.get('meetingLocation').value;
    this.meeting.attendeeEmailSet = this.newAttendeeEmailList;
    this.httpService.updateMeeting(oldName, this.meeting).subscribe();
  }

  addMinute() {
    this.minute = new Minutes();
    this.minute.title = this.addMinuteForm.get('minuteTitle').value;
    this.minute.meetingName = this.meeting.name;
    this.minute.taskSet = this.newTaskSet;
    this.minute.absentEmailSet = this.newAbsentEmailSet;
    this.httpService.addMinute(this.meeting.name, this.minute).subscribe();
  }

}
