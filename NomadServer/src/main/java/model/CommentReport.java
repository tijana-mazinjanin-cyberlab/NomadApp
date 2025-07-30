package model;

import DTO.RatingDTO;
import jakarta.persistence.*;
import model.enums.ReportStatus;

@Entity
@Table (name = "comment_reports")
public class CommentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private AppUser reportingAppUser;
    @ManyToOne
    private Rating reportedRating;
    private String reason;
    private ReportStatus reportStatus;

    public CommentReport(){}
    // Constructor
    public CommentReport(AppUser reportingAppUser, AccommodationRating reportedRating, String reason, ReportStatus reportStatus) {
        this.reportingAppUser = reportingAppUser;
        this.reportedRating = reportedRating;
        this.reason = reason;
        this.reportStatus = reportStatus;
    }

    // Getters and setters for each attribute
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public AppUser getReportingUser() {
        return reportingAppUser;
    }

    public void setReportingUser(AppUser reportingAppUser) {
        this.reportingAppUser = reportingAppUser;
    }

    public Rating getReportedRating() {
        return reportedRating;
    }

    public void setReportedComment(Rating reportedComment) {
        this.reportedRating = reportedComment;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ReportStatus getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(ReportStatus reportStatus) {
        this.reportStatus = reportStatus;
    }

    public void copyValues(CommentReport comment){
        this.reportingAppUser = comment.reportingAppUser;
        this.reportedRating = comment.reportedRating;
        this.reason = comment.reason;
        this.reportStatus = comment.reportStatus;
    }
}
