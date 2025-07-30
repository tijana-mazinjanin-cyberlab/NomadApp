package DTO;

import model.enums.ReportStatus;

public class CommentReportIdDTO {
    private Long reportingAppUser;
    private Long reportedComment;
    private String reason;
    private ReportStatus reportStatus;
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommentReportIdDTO(){}

    public Long getReportingAppUser() {
        return reportingAppUser;
    }

    public void setReportingAppUser(Long reportingAppUser) {
        this.reportingAppUser = reportingAppUser;
    }

    public Long getReportedComment() {
        return reportedComment;
    }

    public void setReportedComment(Long reportedComment) {
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
