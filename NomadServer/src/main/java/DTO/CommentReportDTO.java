package DTO;

import model.AppUser;
import model.Comment;
import model.Rating;
import model.enums.ReportStatus;

public class CommentReportDTO {
    private AppUser reportingAppUser;
    private Rating reportedComment;
    private String reason;
    private ReportStatus reportStatus;

    public CommentReportDTO(){}
    public CommentReportDTO(AppUser reportingAppUser, Rating reportedComment, String reason, ReportStatus reportStatus) {
        this.reportingAppUser = reportingAppUser;
        this.reportedComment = reportedComment;
        this.reason = reason;
        this.reportStatus = reportStatus;
    }

    // Getters and setters for each attribute
    public AppUser getReportingUser() {
        return reportingAppUser;
    }

    public void setReportingUser(AppUser reportingAppUser) {
        this.reportingAppUser = reportingAppUser;
    }

    public Rating getReportedComment() {
        return reportedComment;
    }

    public void setReportedComment(Rating reportedComment) {
        this.reportedComment = reportedComment;
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
}
