import { Component } from '@angular/core';
import { CommentReportsService } from '../comment-reports.service';
import { CommentReport } from '../model/comment-report.model';

@Component({
  selector: 'app-admin-comment-reports',
  templateUrl: './admin-comment-reports.component.html',
  styleUrls: ['./admin-comment-reports.component.css']
})
export class AdminCommentReportsComponent {
  reports: CommentReport[] = []
  displayedColumns: string[] = ['reason'];
  constructor(private commentReportService: CommentReportsService){
    var self = this;
    commentReportService.getReports().subscribe({
      next: (data:CommentReport[]) => {
        this.reports = data;
      },
      error: (_) => {console.log("Greska!")}
  })
  }
}
