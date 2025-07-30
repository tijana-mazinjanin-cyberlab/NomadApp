import {EventEmitter, Injectable} from '@angular/core';

import * as Stomp from 'stompjs';
// import * as SockJS from 'sockjs-client';
import {Message, MyNotification} from "./notifications/notification.model";
import {TokenStorage} from "./infrastructure/auth/jwt/token.service";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  private stompClient:any;
  private isLoaded: boolean;
  private isCustomSocketOpened: boolean;
  public messageReceived: EventEmitter<MyNotification> = new EventEmitter<MyNotification>();


    constructor(private tokenStorage: TokenStorage) {
    this.isLoaded = false;
    this.isCustomSocketOpened = false;
    this.initializeWebSocketConnection();
  }

  initializeWebSocketConnection() {
    // serverUrl je vrednost koju smo definisali u registerStompEndpoints() metodi na serveru
    // let ws = new SockJS("ws://localhost:8080/socket");
    this.stompClient = Stomp.client("ws://localhost:8080/socket");
    let that = this;

    this.stompClient.connect({}, function () {
      that.isLoaded = true;
      that.openSocket();

    });

  }

  sendMessageUsingSocket(message: MyNotification) {
      const serializedMessage = this.serializeMessage(message);
      this.stompClient.send("/socket-subscriber/send/message", {}, serializedMessage);
    }

  openGlobalSocket() {
    if (this.isLoaded) {
      this.stompClient.subscribe("/socket-publisher", (message: { body: string; }) => {
        this.handleResult(message);
      });
    }
  }

  openSocket() {
    if (this.isLoaded) {
      this.isCustomSocketOpened = true;
      this.stompClient.subscribe("/socket-publisher/" + +this.tokenStorage.getId()!, (message: { body: string; }) => {
        this.handleResult(message);
      });
    }
  }

  handleResult(message: { body: string; }) {
    if (message.body) {
      let messageResult: MyNotification = JSON.parse(message.body);
        console.log('Received message:', messageResult);
        this.messageReceived.emit(messageResult);
    }
  }

    private serializeMessage(message: MyNotification): string {
        const message2 = {
            "targetAppUser" : message.targetAppUser.toString(),
            "text": message.text,
            "title": message.title,
            "notificationType": message.notificationType,
            "date": message.date
        }
        return JSON.stringify(message2);
    }
}
