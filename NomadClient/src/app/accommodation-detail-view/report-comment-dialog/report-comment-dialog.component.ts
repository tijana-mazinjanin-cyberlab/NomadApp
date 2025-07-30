import { Component } from '@angular/core';
import { Inject } from '@angular/core';
import {
  MatDialog,
  MAT_DIALOG_DATA,
  MatDialogRef,
  MatDialogTitle,
  MatDialogContent,
  MatDialogActions,
  MatDialogClose,
} from '@angular/material/dialog';
import { AccommodationDetailsService } from '../accommodation-details.service';
import { TokenStorage } from 'src/app/infrastructure/auth/jwt/token.service';
@Component({
  selector: 'app-report-comment-dialog',
  templateUrl: './report-comment-dialog.component.html',
  styleUrls: ['./report-comment-dialog.component.css']
})
export class ReportCommentDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<ReportCommentDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private accommodationDetailsService: AccommodationDetailsService,
    private tokenStorage: TokenStorage
  ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
  onClick(): void {
    this.accommodationDetailsService.addCommentReport({
      "reason" : this.data.reason,
      reportingAppUser : +this.tokenStorage.getId()!,
      reportedComment : this.data.commentId,
      reportStatus : 0
    }).subscribe()
    this.dialogRef.close();
  }
}
export interface DialogData {
  reason: string;
  commentId: number;
}