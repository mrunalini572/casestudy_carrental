package com.hexaware.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hexaware.entity.Customer;
import com.hexaware.entity.Lease;
import com.hexaware.entity.Vehicle;
import com.hexaware.exception.CarNotFoundException;
import com.hexaware.exception.CustomerNotFoundException;
import com.hexaware.exception.LeaseNotFoundException;
import com.hexaware.exception.DatabaseException;
import com.hexaware.util.DBUtil;

public class ICarLeaseRepositoryImpl implements ICarLeaseRepository {

    // Car Management
    @Override
    public void addCar(Vehicle vehicle) {
        String sql = "INSERT INTO vehicle (make, model, year, daily_rate, status, passenger_capacity, engine_capacity) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vehicle.getMake());
            stmt.setString(2, vehicle.getModel());
            stmt.setInt(3, vehicle.getYear());
            stmt.setDouble(4, vehicle.getDailyRate());
            stmt.setString(5, vehicle.getStatus());
            stmt.setInt(6, vehicle.getPassengerCapacity());
            stmt.setDouble(7, vehicle.getEngineCapacity());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding car", e);
        }
    }

    @Override
    public void removeCar(int vehicleId) throws CarNotFoundException {
        String sql = "DELETE FROM vehicle WHERE car_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicleId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new CarNotFoundException("Vehicle ID " + vehicleId + " not found.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error removing vehicle: " + e.getMessage(), e);
        }
    }


    @Override
    public List<Vehicle> listAvailableCars() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicle WHERE status = 'available'";
        try (Connection conn = DBUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vehicles.add(new Vehicle(
                        rs.getInt("car_id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getDouble("daily_rate"),
                        rs.getString("status"),
                        rs.getInt("passenger_capacity"),
                        rs.getDouble("engine_capacity")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error listing available cars", e);
        }
        return vehicles;
    }

    @Override
    public List<Vehicle> listRentedCars() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicle WHERE status = 'rented'";
        try (Connection conn = DBUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vehicles.add(new Vehicle(
                        rs.getInt("car_id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getDouble("daily_rate"),
                        rs.getString("status"),
                        rs.getInt("passenger_capacity"),
                        rs.getDouble("engine_capacity")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error listing rented cars", e);
        }
        return vehicles;
    }

    @Override
    public Vehicle findCarById(int carID) throws CarNotFoundException {
        String sql = "SELECT * FROM vehicle WHERE car_id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Vehicle(
                        rs.getInt("car_id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getDouble("daily_rate"),
                        rs.getString("status"),
                        rs.getInt("passenger_capacity"),
                        rs.getDouble("engine_capacity")
                );
            } else {
                throw new CarNotFoundException("Car not found with ID: " + carID);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding car by ID", e);
        }
    }

    // Customer Management
    @Override
    public void addCustomer(Customer customer) {
        String sql = "INSERT INTO customer (first_name, last_name, email, phone_number) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPhoneNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding customer", e);
        }
    }

    @Override
    public void removeCustomer(int customerID) {
        String sql = "DELETE FROM customer WHERE customer_id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error removing customer", e);
        }
    }

    @Override
    public List<Customer> listCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        try (Connection conn = DBUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error listing customers", e);
        }
        return customers;
    }

    @Override
    public Customer findCustomerById(int customerID) throws CustomerNotFoundException {
        String sql = "SELECT * FROM customer WHERE customer_id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number")
                );
            } else {
                throw new CustomerNotFoundException("Customer not found with ID: " + customerID);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding customer by ID", e);
        }
    }

    // Lease Management
    @Override
    public Lease createLease(int customerID, int carID, Date startDate, Date endDate) throws Exception {
        String sql = "INSERT INTO lease (customer_id, car_id, start_date, end_date, status) VALUES (?, ?, ?, ?, 'active')";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, customerID);
            stmt.setInt(2, carID);
            stmt.setDate(3, new java.sql.Date(startDate.getTime()));
            stmt.setDate(4, new java.sql.Date(endDate.getTime()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return new Lease(rs.getInt(1), customerID, carID, startDate, endDate, "active");
            } else {
                throw new Exception("Lease creation failed");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error creating lease", e);
        }
    }

    @Override
    public void returnCar(int leaseID) throws LeaseNotFoundException {
        String sql = "UPDATE lease SET status = 'completed' WHERE lease_id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, leaseID);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new LeaseNotFoundException("Lease not found with ID: " + leaseID);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error returning car", e);
        }
    }

    @Override
    public List<Lease> listActiveLeases() {
        List<Lease> leases = new ArrayList<>();
        String sql = "SELECT * FROM lease WHERE status = 'active'";
        try (Connection conn = DBUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                leases.add(new Lease(
                        rs.getInt("lease_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("car_id"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error listing active leases", e);
        }
        return leases;
    }

    @Override
    public List<Lease> listLeaseHistory() {
        List<Lease> leases = new ArrayList<>();
        String sql = "SELECT * FROM lease";
        try (Connection conn = DBUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                leases.add(new Lease(
                        rs.getInt("lease_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("car_id"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error listing lease history", e);
        }
        return leases;
    }

    // Payment Handling
    @Override
    public void recordPayment(Lease lease, double amount) {
        String sql = "INSERT INTO payment (lease_id, payment_date, amount) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lease.getLeaseID());
            stmt.setDate(2, new java.sql.Date(new Date().getTime()));
            stmt.setDouble(3, amount);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error recording payment", e);
        }
    }
}



