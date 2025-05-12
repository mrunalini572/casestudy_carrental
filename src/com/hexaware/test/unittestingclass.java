package com.hexaware.test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.*;

import com.hexaware.dao.ICarLeaseRepositoryImpl;
import com.hexaware.entity.Vehicle;
import com.hexaware.util.DBUtil;

class CarRentalTestFile {

    private static ICarLeaseRepositoryImpl carLeaseRepository;

   
    public static void setup() {
        // Ensure the repository is correctly initialized before tests
        carLeaseRepository = new ICarLeaseRepositoryImpl();
    }

    // Test DB Connection
    
    void testConnection() {
        try {
            Connection actualValue = DBUtil.getConnection();
            assertNotNull(actualValue, "DB Connection failed");
        } catch (SQLException e) {
            fail("Database connection error: " + e.getMessage());
        }
    }

    // Test Adding a Vehicle
    
    public void testAddVehicle() {
        Vehicle vehicle = new Vehicle(1, "Toyota", "Corolla", 2021, 40.0, "available", 5, 1.8);
        try {
            // Assuming addCar method does not return the added vehicle, it might just execute update
            carLeaseRepository.addCar(vehicle);

            // Fetch the vehicle by ID to confirm it was added
            Vehicle result = carLeaseRepository.findCarById(1);
            assertNotNull(result, "Vehicle should not be null");
            assertEquals("Toyota", result.getMake(), "Make should be Toyota");
            assertEquals("Corolla", result.getModel(), "Model should be Corolla");
            assertEquals(2021, result.getYear(), "Year should be 2021");
            assertEquals(40.0, result.getDailyRate(), "Daily rate should be 40.0");
            assertEquals("available", result.getStatus(), "Status should be available");
        } catch (SQLException e) {
            fail("SQL error during vehicle insertion: " + e.getMessage());
        } catch (Exception e) {
            fail("Unexpected error: " + e.getMessage());
        }
    }
}

