export class Project {
  name : string;
  description: string;
  meetingSet: string[];
  participantSet: string[];
  ownerName: string;
  taskSet: string[];
}

export class Task {
  info: TaskDescription;
  developerEmail: string;
  projectName: string;
}

export class Meeting {
  name: string;
  description: string;
  projectName: string;
  location: string;
  date: string;
  startTime: string;
  endTime: string;
  minuteName: string;
  chairPersonEmail: string;
  attendeeEmailSet: string[];
}

export class Minutes {
  title: string;
  meetingName: string;
  absentEmailSet: string[];
  taskSet: TaskDescription[];
}

export class TaskDescription {
  name: string;
  description: string;
  estimatingDate: Date;
}

export class User {
  name: string;
  email: string;
  roleSet: string[];
  projectNameSet: string[];
  meetingNameSet: string[];
  taskNameSet: string[];
}
