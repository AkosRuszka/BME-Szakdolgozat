import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Meeting, Minutes, Project, Task, User} from "../models/dtos";

@Injectable({
  providedIn: 'root'
})
export class CommunicationService {

  private url = 'http://localhost:9003';
  private meetingURL = this.url + '/meeting';
  private projectURL = this.url + '/project';
  private taskURL = this.url + '/task';
  private userURL = this.url + '/user';

  private httpOptions = {
    headers: new HttpHeaders({
      'Authorization': 'Basic dGVzdEBnbWFpbC5jb206amVsc3pv',
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': 'localhost:4200',
      'Access-Control-Allow-Headers': 'Content-Type'
    }),
    params: new HttpParams()
  };

  constructor(private http: HttpClient) { }

  getUserInfo() : Observable<User> {
    return this.http.get<User>(this.userURL, this.httpOptions);
  }

  getProjects(pageSize: number, pageNumber: number) : Observable<Project[]> {
    return this.http.get<Project[]>(this.projectURL + this.createPagePath(pageSize, pageNumber) ,this.httpOptions);
  }

  getProject(name: string): Observable<Project> {
    return this.http.get<Project>(this.projectURL + '/' + name, this.httpOptions);
  }

  addNewProject(newProject: Project) {
    return this.http.post(this.projectURL, newProject ,this.httpOptions);
  }

  updateProject(projectName: string, projectDto: Project) {
    return this.http.put(this.projectURL + '/' + projectName, projectDto, this.httpOptions);
  }

  deleteProject(projectName: string) {
    return this.http.delete(this.projectURL + '/' + projectName, this.httpOptions);
  }

  getTasksForProject(projectName: string) : Observable<Task[]>{
    return this.http.get<Task[]>(this.projectURL + '/task', this.httpOptions);
  }

  getMeetings(pageSize: number, pageNumber: number) : Observable<Meeting[]> {
    return this.http.get<Meeting[]>(this.meetingURL + this.createPagePath(pageSize, pageNumber), this.httpOptions);
  }

  getMeeting(name: string) : Observable<Meeting> {
    return this.http.get<Meeting>(this.meetingURL + '/' +name, this.httpOptions);
  }

  addNewMeeting(newMeeting: Meeting) {
    this.http.post(this.meetingURL, newMeeting ,this.httpOptions);
  }

  updateMeeting(meetingName: string, meetingDto : Meeting) {
    this.http.put(this.meetingURL + '/' + meetingName, meetingDto, this.httpOptions);
  }

  deleteMeeting(meetingName: string) {
    this.http.delete(this.meetingURL + '/' + meetingName, this.httpOptions);
  }

  addMinute(meetingName: string, minutes: Minutes) {
    this.http.post(this.meetingURL + '/' + meetingName, minutes, this.httpOptions);
  }

  updateMinute(meetingName: string, minutes: Minutes) {
    this.http.post(this.meetingURL + '/' + meetingName + '/' + minutes.title, minutes, this.httpOptions);
  }

  getTasks(pageSize: number, pageNumber: number) : Observable<Task[]> {
    return this.http.get<Task[]>(this.taskURL + this.createPagePath(pageSize, pageNumber), this.httpOptions);
  }

  getTask(taskName: string) : Observable<Task> {
    return this.http.get<Task>(this.taskURL + '/' + taskName, this.httpOptions);
  }

  newTask(dto : Task) {
    return this.http.post(this.taskURL, dto, this.httpOptions);
  }

  updateTask(taskName: string, dto: Task) {
    return this.http.put(this.taskURL + '/' + taskName, dto, this.httpOptions);
  }

  deleteTask(taskName: string) {
    return this.http.delete(this.taskURL + '/' + taskName, this.httpOptions);
  }

  getRegisteredClients() : Observable<Map<string, string>> {
    return this.http.get<Map<string, string>>(this.url + '/authentication/getOAuthClients', this.httpOptions);
  }

  private createPagePath(pageSize: number, pageNumber: number): string {
    return '?size=' + pageSize + '&page=' + pageNumber;
  }
}
