import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.scss']
})
export class LoaderComponent implements OnInit {

  @Input() message = '';
  static isLoading = false;

  constructor() { }

  ngOnInit() {
  }

  isLoading() {
    return LoaderComponent.isLoading;
  }

  static loading() {
    LoaderComponent.isLoading = true;
  }

  static loaded() {
    LoaderComponent.isLoading = false;
  }

}
