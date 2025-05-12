package com.hexaware.dao;

import java.util.Date;
import java.util.List;

import com.hexaware.entity.Customer;
import com.hexaware.entity.Lease;
import com.hexaware.entity.Vehicle;
import com.hexaware.exception.CarNotFoundException;
import com.hexaware.exception.CustomerNotFoundException;
import com.hexaware.exception.LeaseNotFoundException;

public interface ICarLeaseRepository {

    // Car Management
    void addCar(Vehicle vehicle);
    void removeCar(int carID);
    List<Vehicle> listAvailableCars();
    List<Vehicle> listRentedCars();
    Vehicle findCarById(int carID) throws CarNotFoundException;

    // Customer Management
    void addCustomer(Customer customer);
    void removeCustomer(int customerID);
    List<Customer> listCustomers();
    Customer findCustomerById(int customerID) throws CustomerNotFoundException;

    // Lease Management
    Lease createLease(int customerID, int carID, Date startDate, Date endDate) throws Exception;
    void returnCar(int leaseID) throws LeaseNotFoundException;
    List<Lease> listActiveLeases();
    List<Lease> listLeaseHistory();

    // Payment Handling
    void recordPayment(Lease lease, double amount);
}




