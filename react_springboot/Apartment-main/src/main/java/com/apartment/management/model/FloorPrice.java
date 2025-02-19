package com.apartment.management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class FloorPrice {

    @Id
    private int floor; // ใช้ floor เป็น Primary Key เพราะมีแค่ 4 ชั้น

    @Column(nullable = false)
    private double price; // ราคาห้องของชั้นนี้

    // Constructor
    public FloorPrice() {}

    public FloorPrice(int floor, double price) {
        this.floor = floor;
        this.price = price;
    }

    // Getters and Setters
    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

