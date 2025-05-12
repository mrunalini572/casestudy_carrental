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
import com.hexaware.util.DatabaseConnection;

public class ICarLeaseRepositoryImpl implements ICarLeaseRepository {

    private static final String URL = "jdbc:mysql://localhost:3306/VehicleRentalDB";
    private static final String USER = "root";
    private static final String PASSWORD = "Mrunalini";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Car Management
    @Override
    public void addCar(Vehicle vehicle) {
        String sql = "INSERT INTO vehicle (make, model, year, price_per_day) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vehicle.getMake());
            stmt.setString(2, vehicle.getModel());
            stmt.setInt(3, vehicle.getYear());
            stmt.setDouble(4, vehicle.getPricePerDay());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding car", e);
        }
    }

    @Override
    public void removeCar(int carID) {
        String sql = "DELETE FROM vehicle WHERE car_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error removing car", e);
        }
    }

    @Override
    public List<Vehicle> listAvailableCars() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicle WHERE status = 'available'";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vehicles.add(new Vehicle(rs.getInt("car_id"), rs.getString("make"), rs.getString("model"),
                        rs.getInt("year"), rs.getDouble("price_per_day")));
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
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vehicles.add(new Vehicle(rs.getInt("car_id"), rs.getString("make"), rs.getString("model"),
                        rs.getInt("year"), rs.getDouble("price_per_day")));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error listing rented cars", e);
        }
        return vehicles;
    }

    @Override
    public Vehicle findCarById(int carID) throws Exception {
        String sql = "SELECT * FROM vehicle WHERE car_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Vehicle(rs.getInt("car_id"), rs.getString("make"), rs.getString("model"),
                        rs.getInt("year"), rs.getDouble("price_per_day"));
            } else {
                throw new Exception("Car not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding car by ID", e);
        }
    }

    // Customer Management
    @Override
    public void addCustomer(Customer customer) {
        String sql = "INSERT INTO customer (name, email, phone_number) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getPhoneNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding customer", e);
        }
    }

    @Override
    public void removeCustomer(int customerID) {
        String sql = "DELETE FROM customer WHERE customer_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error removing customer", e);
        }
    }

    @Override
    public List<Customer> listCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customer";  // SQL query to fetch all customers from the database
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Customer customer = new Customer(
                    resultSet.getInt("customer_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email"),
                    resultSet.getString("phone_number")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }


    @Override
    public Customer findCustomerById(int customerID) throws Exception {
        String sql = "SELECT * FROM customer WHERE customer_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(rs.getInt("customer_id"), rs.getString("name"), rs.getString("email"),
                        rs.getString("phone_number"));
            } else {
                throw new Exception("Customer not found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding customer by ID", e);
        }
    }

    // Lease Management
    @Override
    public Lease createLease(int customerID, int carID, Date startDate, Date endDate) throws Exception {
        String sql = "INSERT INTO lease (customer_id, car_id, start_date, end_date, status) VALUES (?, ?, ?, ?, 'active')";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
    public void returnCar(int leaseID) throws Exception {
        String sql = "UPDATE lease SET status = 'completed' WHERE lease_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, leaseID);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new Exception("Lease not found or already completed");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error returning car", e);
        }
    }

    @Override
    public List<Lease> listActiveLeases() {
        List<Lease> leases = new ArrayList<>();
        String sql = "SELECT * FROM lease WHERE status = 'active'";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                leases.add(new Lease(rs.getInt("lease_id"), rs.getInt("customer_id"), rs.getInt("car_id"),
                        rs.getDate("start_date"), rs.getDate("end_date"), rs.getString("status")));
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
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                leases.add(new Lease(rs.getInt("lease_id"), rs.getInt("customer_id"), rs.getInt("car_id"),
                        rs.getDate("start_date"), rs.getDate("end_date"), rs.getString("status")));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error listing lease history", e);
        }
        return leases;
    }

    // Payment Handling
    @Override
    public void recordPayment(Lease lease, double amount) {
        String sql = "INSERT INTO payment (lease_id, amount) VALUES (?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lease.getLeaseID());
            stmt.setDouble(2, amount);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error recording payment", e);
        }
    }
}

