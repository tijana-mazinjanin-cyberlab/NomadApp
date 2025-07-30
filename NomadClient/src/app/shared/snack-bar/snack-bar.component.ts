import {Component, Input} from '@angular/core';
import {SnackBarService} from "../snack-bar.service";

@Component({
  selector: 'app-snack-bar',
  templateUrl: './snack-bar.component.html',
  styleUrls: ['./snack-bar.component.css']
})
export class SnackBarComponent {
  @Input() errorMessage: string = '';
  constructor( private snackService: SnackBarService ) {
  }
  ngOnInit(): void {
    this.snackService.errorMessage$.subscribe(data => {
      this.errorMessage = data;
    });
  }
}
