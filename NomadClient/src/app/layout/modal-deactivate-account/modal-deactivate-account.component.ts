import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-modal-deactivate-account',
  templateUrl: './modal-deactivate-account.component.html',
  styleUrls: ['./modal-deactivate-account.component.css']
})
export class ModalDeactivateAccountComponent {
  @Output() updatedResponse = new EventEmitter<boolean> ();
  SendResponse(response: boolean) {
      this.updatedResponse.emit(response);
  }
}
