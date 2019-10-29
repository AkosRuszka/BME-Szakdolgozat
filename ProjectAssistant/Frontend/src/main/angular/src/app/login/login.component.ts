import {Component, OnInit} from '@angular/core';
import {CommunicationService} from "../services/communication.service";
import {ActivatedRoute} from "@angular/router";
import {TokenService} from "../services/token.service";
import {Location} from "@angular/common";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  clients = new Map<String, String>();

  constructor(private communication: CommunicationService,
              private route: ActivatedRoute,
              private location: Location,
              private tokenStore: TokenService) {
  }

  ngOnInit() {
    this.getRegisteredClients();
    setTimeout(() => {
      this.route.queryParams.subscribe(
        data => {
          var token = data['token'];
          if(token != undefined) {
            this.tokenStore.setToken(token);
            this.location.replaceState(location.pathname);
          }
        }
      )
    });
  }

  private getRegisteredClients() {
    this.communication.getRegisteredClients().subscribe(
      result => {
        this.clients = result;
      },
      error1 => {
        console.log("Hiba történt a cliensek lekérdezésekkor", error1);
      }
    );
  }

}
