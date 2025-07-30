import { Component, OnInit } from '@angular/core';
import { CommentReportsService } from 'src/app/account/comment-reports.service';
import { CommentReport } from 'src/app/account/model/comment-report.model';
import { CommentService } from '../comment.service';
import { Review } from '../model/accommodationDetails.model';
@Component({
  selector: 'app-comment-reports',
  templateUrl: './comment-reports.component.html',
  styleUrls: ['./comment-reports.component.css']
})
export class CommentReportsComponent implements OnInit{
  reports? : CommentReport[];
  loaded : boolean = false;
  constructor(private commentReportsService : CommentReportsService, private commentService : CommentService){

  }
  ngOnInit(): void {
   this.loadReports()
  }
  accept(id: number) : void{
    this.commentReportsService.accept(id).subscribe({
      next: (_) => {
        this.loadReports()

      }
    });
  }
  deny(id: number) : void{
    this.commentReportsService.archive(id).subscribe({
      next: (_) => {
        this.loadReports()

      }
    });
  }
  loadReports() : void {
    this.commentReportsService.getReports().subscribe({
      next: (value: CommentReport[]) => {
        this.reports = value;
        for (let report of this.reports){
          this.commentService.getComment(report.reportedComment).subscribe({
            next: (review : Review) => {
              report.reportedCommentDetails = review;

            }
          })
        }
     },
      error: (_) => {console.log("Greska!")}
    })
  }
}