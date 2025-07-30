
export interface MyNotification{
  "id"? : number;
  "targetAppUser" : number;
  "text":string;
  "title":string;
  "notificationType":string;
  "date": Date;
}

export interface NotificationsPreferencesGuest {
  "REQUEST_RESPONSE": boolean;
}

export interface NotificationsPreferencesHost {
  "NEW_ACCOMMODATION_RATING": boolean;
  "NEW_RATING": boolean;
  "NEW_RESERVATION": boolean;
  "RESERVATION_CANCELED": boolean;
}

export interface Message {
  message: string,
  fromId: string,
  toId: string,
}
