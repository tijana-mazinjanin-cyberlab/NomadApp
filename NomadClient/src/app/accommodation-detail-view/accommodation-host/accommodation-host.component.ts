import {Component, Input} from '@angular/core';
import {UserReport} from "../model/userReport.model";
import {AccountService} from "../../account/account.service";
import {User} from "../../account/model/user.model";
import {UserReportService} from "../user-report.service";
import {Review} from "../model/accommodationDetails.model";
import {CommentService} from "../comment.service";

@Component({
  selector: 'app-accommodation-host',
  templateUrl: './accommodation-host.component.html',
  styleUrls: ['./accommodation-host.component.css']
})
export class AccommodationHostComponent {

  @Input() hostId?: number;

  reportReason: string = "";

  reportFormVisible: boolean = false;
  ratingFormVisible:boolean = false;

  reviewsHost?: Review[];

  user: User = {} as User;

  constructor(private accountService: AccountService, private reportService: UserReportService, private commentService: CommentService) {}

  ngOnInit(): void {
    this.accountService.getLoggedUser().subscribe({
      next: (data: User) => { this.user = data; },
      error: () => { console.log("Error while reading logged user!"); }
    })

    this.getCommentsHost();
  }

  showReportForm() {
    this.reportFormVisible = !this.reportFormVisible;
    this.ratingFormVisible = false;
  }

  showRateForm() {
    this.ratingFormVisible = !this.ratingFormVisible;
    this.reportFormVisible = false;
  }

  getCommentsHost(): void {
    this.commentService.getHostComments(this.hostId!).subscribe({
      next: (data:Review[]) => {
        this.reviewsHost = data;
      }
    })
  }

  reportUser() {
    const report: UserReport = {
      "reportingUser" : this.user.id,
      "reportedUser" : this.hostId!,
      "reason" : this.reportReason,
      "reportStatus": "PENDING"
    }

    this.reportService.reportUser(report).subscribe({
      next: () => {
          console.log("Success");
          this.reportReason = "";
        },
      error: () => {console.log("Error!!")}
    })
  }


}
