package com.apartment.management.dto;

import java.time.LocalDate;

import com.apartment.management.model.Billing;

public class BillingDTO {
    private Long roomId;
    private String roomNumber;
    private Double waterMeterStart;
    private Double waterMeterEnd;
    private Double electricMeterStart;
    private Double electricMeterEnd;
    private Double waterUsage;
    private Double electricUsage;
    private Double waterCharge;
    private Double electricCharge;
    private Double parkingFee;
    private Double cableFee;
    private Double commonFee;
    private Double totalAmount;
    private LocalDate billingMonth;

    public BillingDTO(Billing billing) {
        this.roomId = billing.getRoom() != null ? billing.getRoom().getId() : null;
        this.roomNumber = billing.getRoom() != null ? billing.getRoom().getRoomNumber() : "N/A";

        // ✅ เปลี่ยนการตรวจสอบ null เป็นการใช้ค่าเริ่มต้น 0.0
        this.waterMeterStart = billing.getWaterMeterStart();
        this.waterMeterEnd = billing.getWaterMeterEnd();
        this.electricMeterStart = billing.getElectricMeterStart();
        this.electricMeterEnd = billing.getElectricMeterEnd();
        this.waterUsage = billing.getWaterUsage();
        this.electricUsage = billing.getElectricUsage();

        // ✅ ป้องกัน null สำหรับค่าใช้จ่าย (BigDecimal → Double)
        this.waterCharge = billing.getWaterBill() != null ? billing.getWaterBill().doubleValue() : 0.0;
        this.electricCharge = billing.getElectricBill() != null ? billing.getElectricBill().doubleValue() : 0.0;
        this.parkingFee = billing.getParkingFee() != null ? billing.getParkingFee().doubleValue() : 0.0;
        this.cableFee = billing.getCableFee() != null ? billing.getCableFee().doubleValue() : 0.0;
        this.commonFee = billing.getCommonFee() != null ? billing.getCommonFee().doubleValue() : 0.0;

        // ✅ คำนวณยอดรวม (ป้องกัน null)
        this.totalAmount = billing.getTotalBill() != null ? billing.getTotalBill().doubleValue() : 
            (this.waterCharge + this.electricCharge + this.parkingFee + this.cableFee + this.commonFee);

        // ✅ แปลงเดือนเป็น LocalDate (ป้องกัน error)
        this.billingMonth = LocalDate.parse(billing.getMonth() + "-01");
    }

    // ✅ Getters
    public Long getRoomId() { return roomId; }
    public String getRoomNumber() { return roomNumber; }
    public Double getWaterMeterStart() { return waterMeterStart; }
    public Double getWaterMeterEnd() { return waterMeterEnd; }
    public Double getElectricMeterStart() { return electricMeterStart; }
    public Double getElectricMeterEnd() { return electricMeterEnd; }
    public Double getWaterUsage() { return waterUsage; }
    public Double getElectricUsage() { return electricUsage; }
    public Double getWaterCharge() { return waterCharge; }
    public Double getElectricCharge() { return electricCharge; }
    public Double getParkingFee() { return parkingFee; }
    public Double getCableFee() { return cableFee; }
    public Double getCommonFee() { return commonFee; }
    public Double getTotalAmount() { return totalAmount; }
    public LocalDate getBillingMonth() { return billingMonth; }

    @Override
    public String toString() {
        return "BillingDTO{" +
                "roomId=" + roomId +
                ", roomNumber='" + roomNumber + '\'' +
                ", waterMeterStart=" + waterMeterStart +
                ", waterMeterEnd=" + waterMeterEnd +
                ", electricMeterStart=" + electricMeterStart +
                ", electricMeterEnd=" + electricMeterEnd +
                ", waterUsage=" + waterUsage +
                ", electricUsage=" + electricUsage +
                ", waterCharge=" + waterCharge +
                ", electricCharge=" + electricCharge +
                ", parkingFee=" + parkingFee +
                ", cableFee=" + cableFee +
                ", commonFee=" + commonFee +
                ", totalAmount=" + totalAmount +
                ", billingMonth=" + billingMonth +
                '}';
    }
}
