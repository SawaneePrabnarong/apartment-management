package com.apartment.management.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private Room room;

    private String month;
    @Column(name = "room_number")
    private String roomNumber;

    @Column(nullable = false, columnDefinition = "VARCHAR(10) DEFAULT 'Unpaid'")
    private String status = "Unpaid"; // ✅ กำหนดค่า Default ใน Java

    // @Column(nullable = false, columnDefinition = "VARCHAR(10) DEFAULT 'Unpaid'")
    // private String status;
    private BigDecimal roomPrice;
    private BigDecimal waterBill; // ค่าใช้จ่ายน้ำ
    private BigDecimal electricBill; // ค่าใช้จ่ายไฟฟ้า
    private BigDecimal totalBill; // รวมค่าใช้จ่ายทั้งหมด // ✅
                                  // เพิ่มฟิลด์ใหม่เพื่อรองรับค่ามิเตอร์และค่าใช้จ่ายเพิ่มเติม
    private double waterMeterStart;
    private double waterMeterEnd;
    private double electricMeterStart;
    private double electricMeterEnd;
    private double waterUsage;
    private double electricUsage;
    private BigDecimal parkingFee;
    private BigDecimal cableFee;
    private BigDecimal commonFee;

    // Getters and Setters

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(BigDecimal roomPrice) {
        this.roomPrice = roomPrice;
    }

    public BigDecimal getWaterBill() {
        return waterBill;
    }

    public void setWaterBill(BigDecimal waterBill) {
        this.waterBill = waterBill;
    }

    public BigDecimal getElectricBill() {
        return electricBill;
    }

    public void setElectricBill(BigDecimal electricBill) {
        this.electricBill = electricBill;
    }

    public BigDecimal getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(BigDecimal totalBill) {
        this.totalBill = totalBill;
    }

    public double getWaterMeterStart() {
        return waterMeterStart;
    }

    public void setWaterMeterStart(double waterMeterStart) {
        this.waterMeterStart = waterMeterStart;
    }

    public double getWaterMeterEnd() {
        return waterMeterEnd;
    }

    public void setWaterMeterEnd(double waterMeterEnd) {
        this.waterMeterEnd = waterMeterEnd;
    }

    public double getElectricMeterStart() {
        return electricMeterStart;
    }

    public void setElectricMeterStart(double electricMeterStart) {
        this.electricMeterStart = electricMeterStart;
    }

    public double getElectricMeterEnd() {
        return electricMeterEnd;
    }

    public void setElectricMeterEnd(double electricMeterEnd) {
        this.electricMeterEnd = electricMeterEnd;
    }

    public double getWaterUsage() {
        return waterUsage;
    }

    public void setWaterUsage(double waterUsage) {
        this.waterUsage = waterUsage;
    }

    public double getElectricUsage() {
        return electricUsage;
    }

    public void setElectricUsage(double electricUsage) {
        this.electricUsage = electricUsage;
    }

    public BigDecimal getParkingFee() {
        return parkingFee;
    }

    public void setParkingFee(BigDecimal parkingFee) {
        this.parkingFee = parkingFee;
    }

    public BigDecimal getCableFee() {
        return cableFee;
    }

    public void setCableFee(BigDecimal cableFee) {
        this.cableFee = cableFee;
    }

    public BigDecimal getCommonFee() {
        return commonFee;
    }

    public void setCommonFee(BigDecimal commonFee) {
        this.commonFee = commonFee;
    }
}