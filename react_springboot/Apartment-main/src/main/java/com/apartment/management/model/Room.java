package com.apartment.management.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String roomNumber;

    @Column(nullable = false)
    private int floor; // ระบุชั้นของห้อง

    @ManyToOne
    @JoinColumn(name = "floor", referencedColumnName = "floor", insertable = false, updatable = false)
    private FloorPrice floorPrice; // เชื่อมกับราคาห้องตามชั้น

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WaterMeter> waterMeters;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElectricMeter> electricMeters;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Billing> billings;

    // Constructor
    public Room() {
    }

    // public Room(String roomNumber) {
    // this.roomNumber = roomNumber;
    // }
    public Room(String roomNumber, int floor) {
        this.roomNumber = roomNumber;
        this.floor = floor;
    }

    // Getters and Setters
    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public FloorPrice getFloorPrice() {
        return floorPrice;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public List<WaterMeter> getWaterMeters() {
        return waterMeters;
    }

    public void setWaterMeters(List<WaterMeter> waterMeters) {
        this.waterMeters = waterMeters;
    }

    public List<ElectricMeter> getElectricMeters() {
        return electricMeters;
    }

    public void setElectricMeters(List<ElectricMeter> electricMeters) {
        this.electricMeters = electricMeters;
    }

    public List<Billing> getBillings() {
        return billings;
    }

    public void setBillings(List<Billing> billings) {
        this.billings = billings;
    }
}
