package com.hexaware.entity;

public class Vehicle {

    private int vehicleID;
    private String make;
    private String model;
    private int year;
    private double dailyRate;
    private String status; // e.g., "available", "rented"
    private int passengerCapacity;
    private double engineCapacity;

    // Default Constructor
    public Vehicle() {}

    // Full Constructor
    public Vehicle(int vehicleID, String make, String model, int year, double dailyRate,
                   String status, int passengerCapacity, double engineCapacity) {
        this.vehicleID = vehicleID;
        this.make = make;
        this.model = model;
        this.year = year;
        this.dailyRate = dailyRate;
        this.status = status;
        this.passengerCapacity = passengerCapacity;
        this.engineCapacity = engineCapacity;
    }

    // Constructor without vehicleID and status (e.g., for insertion use)
    public Vehicle(String make, String model, int year, double dailyRate,
                   int passengerCapacity, double engineCapacity) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.dailyRate = dailyRate;
        this.passengerCapacity = passengerCapacity;
        this.engineCapacity = engineCapacity;
        this.status = "available"; // default status
    }

    // Getters and Setters
    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public void setPassengerCapacity(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }

    public double getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(double engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleID=" + vehicleID +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", dailyRate=" + dailyRate +
                ", status='" + status + '\'' +
                ", passengerCapacity=" + passengerCapacity +
                ", engineCapacity=" + engineCapacity +
                '}';
    }
}
