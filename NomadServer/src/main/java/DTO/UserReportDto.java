package DTO;

import model.enums.ReportStatus;

public class UserReportDto {

    private Long reportingUser;
    private Long reportedUser;
    private String reason;
    private ReportStatus reportStatus;

    public UserReportDto() {}

    public UserReportDto(Long reportingUser, Long reportedUser, String reason, ReportStatus reportStatus) {
        this.reportingUser = reportingUser;
        this.reportedUser = reportedUser;
        this.reason = reason;
        this.reportStatus = reportStatus;
    }

    public Long getReportingUser() {
        return reportingUser;
    }

    public void setReportingUser(Long reportingUser) {
        this.reportingUser = reportingUser;
    }

    public Long getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(Long reportedUser) {
        this.reportedUser = reportedUser;
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
