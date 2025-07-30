package DTO;

public class ReportDTO {
    private Double profit;
    private int reservationNumber;
    private Long accommodation_id;
    private Integer month;

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public ReportDTO(Double profit, int reservationNumber, Long accommodation_id) {
        this.profit = profit;
        this.reservationNumber = reservationNumber;
        this.accommodation_id = accommodation_id;
    }

    public ReportDTO() {
        this.profit = 0.0;
        this.reservationNumber = 0;
    }
    public void addProfit(Double price){
        this.profit += price;
    }
    public void increaseResNum(){
        this.reservationNumber++;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public int getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(int reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public Long getAccommodation_id() {
        return accommodation_id;
    }

    public void setAccommodation_id(Long accommodation_id) {
        this.accommodation_id = accommodation_id;
    }
}
