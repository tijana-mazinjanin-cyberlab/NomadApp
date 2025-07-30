package model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Embeddable
public class DateRange {
    @Column (name = "start_date")
    private Date startDate;
    @Column(name = "finish_date")
    private Date finishDate;

    // Constructor
    public DateRange(Date startDate, Date finishDate) {
        if (startDate == null || finishDate == null || startDate.after(finishDate)) {
            throw new IllegalArgumentException("Invalid date range. Start date must be before the end date.");
        }
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public DateRange(String startDate, String finishDate)  {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startDate);
            Date finish = dateFormat.parse(finishDate);

            if (start == null || finish == null || start.after(finish)) {
                throw new IllegalArgumentException("Invalid date range. Start date must be before the end date.");
            }
            this.startDate = start;
            this.finishDate = finish;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public DateRange() {}

    public boolean overlaps(DateRange comparedDateRange) {
        return !this.finishDate.before(comparedDateRange.getStartDate()) && !this.startDate.after(comparedDateRange.getFinishDate());
    }

    public boolean isInRange(DateRange comparedDateRange) {
        return !this.startDate.before(comparedDateRange.getStartDate()) && !this.finishDate.after(comparedDateRange.getFinishDate());
     }

    // Getters for each attribute
    public Date getStartDate() {
        return startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    @Override
    public String toString() {
        return "[ " + startDate.toString() + ", " + finishDate.toString() + " ]";
    }
}
