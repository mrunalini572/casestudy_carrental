package com.hexaware.main;

import com.hexaware.dao.ICarLeaseRepository;
import com.hexaware.dao.ICarLeaseRepositoryImpl;
import com.hexaware.entity.Customer;
import com.hexaware.entity.Lease;
import com.hexaware.entity.Vehicle;
import com.hexaware.exception.CarNotFoundException;
import com.hexaware.exception.CustomerNotFoundException;
import com.hexaware.exception.LeaseNotFoundException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MainModule {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ICarLeaseRepository repository = new ICarLeaseRepositoryImpl(); // Repository instance

    public static void main(String[] args) {
        System.out.println("Welcome to the Car Rental System");
        boolean exit = false;

        while (!exit) {
            printMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1": addVehicle(); break;
                    case "2": removeVehicle(); break;
                    case "3": listAvailableVehicles(); break;
                    case "4": listRentedVehicles(); break;
                    case "5": findVehicleById(); break;
                    case "6": addCustomer(); break;
                    case "7": removeCustomer(); break;
                    case "8": listCustomers(); break;
                    case "9": findCustomerById(); break;
                    case "10": createLease(); break;
                    case "11": returnCar(); break;
                    case "12": listActiveLeases(); break;
                    case "13": listLeaseHistory(); break;
                    case "14": recordPayment(); break;
                    case "0":
                        System.out.println("Exiting the application. Goodbye!");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice! Please enter a valid option.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
            System.out.println();
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("------ Main Menu ------");
        System.out.println("1. Add Vehicle");
        System.out.println("2. Remove Vehicle");
        System.out.println("3. List Available Vehicles");
        System.out.println("4. List Rented Vehicles");
        System.out.println("5. Find Vehicle By ID");
        System.out.println("6. Add Customer");
        System.out.println("7. Remove Customer");
        System.out.println("8. List Customers");
        System.out.println("9. Find Customer By ID");
        System.out.println("10. Create Lease");
        System.out.println("11. Return Car");
        System.out.println("12. List Active Leases");
        System.out.println("13. List Lease History");
        System.out.println("14. Record Payment");
        System.out.println("0. Exit");
    }

    private static void addVehicle() {
        System.out.println("Enter Vehicle details:");
        System.out.print("Make: ");
        String make = scanner.nextLine();
        System.out.print("Model: ");
        String model = scanner.nextLine();
        System.out.print("Year (e.g., 2022): ");
        int year = Integer.parseInt(scanner.nextLine());
        System.out.print("Daily Rate: ");
        double dailyRate = Double.parseDouble(scanner.nextLine());
        System.out.print("Status (available/notAvailable): ");
        String status = scanner.nextLine();
        System.out.print("Passenger Capacity: ");
        int passengerCapacity = Integer.parseInt(scanner.nextLine());
        System.out.print("Engine Capacity: ");
        double engineCapacity = Double.parseDouble(scanner.nextLine());

        Vehicle vehicle = new Vehicle(0, make, model, year, dailyRate, status, passengerCapacity, engineCapacity);
        repository.addCar(vehicle);
        System.out.println("Vehicle added successfully.");
    }

    private static void removeVehicle() {
        System.out.print("Enter Vehicle ID to remove: ");
        int vehicleId = Integer.parseInt(scanner.nextLine());
        repository.removeCar(vehicleId);
        System.out.println("Vehicle removed successfully.");
    }

    private static void listAvailableVehicles() {
        List<Vehicle> vehicles = repository.listAvailableCars();
        if (vehicles.isEmpty()) {
            System.out.println("No available vehicles.");
            return;
        }
        vehicles.forEach(v -> System.out.printf("ID: %d | %s %s | Year: %d | Rate: %.2f\n",
                v.getVehicleID(), v.getMake(), v.getModel(), v.getYear(), v.getDailyRate()));
    }

    private static void listRentedVehicles() {
        List<Vehicle> vehicles = repository.listRentedCars();
        if (vehicles.isEmpty()) {
            System.out.println("No rented vehicles.");
            return;
        }
        vehicles.forEach(v -> System.out.printf("ID: %d | %s %s | Year: %d | Rate: %.2f\n",
                v.getVehicleID(), v.getMake(), v.getModel(), v.getYear(), v.getDailyRate()));
    }

    private static void findVehicleById() throws CarNotFoundException {
        System.out.print("Enter Vehicle ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Vehicle v = repository.findCarById(id);
        System.out.printf("Found: ID: %d | %s %s | Year: %d | Status: %s | Rate: %.2f\n",
                v.getVehicleID(), v.getMake(), v.getModel(), v.getYear(), v.getStatus(), v.getDailyRate());
    }

    private static void addCustomer() {
        System.out.println("Enter Customer details:");
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        Customer customer = new Customer(0, firstName, lastName, email, phone);
        repository.addCustomer(customer);
        System.out.println("Customer added successfully.");
    }

    private static void removeCustomer() {
        System.out.print("Enter Customer ID to remove: ");
        int id = Integer.parseInt(scanner.nextLine());
        repository.removeCustomer(id);
        System.out.println("Customer removed successfully.");
    }

    private static void listCustomers() {
        try {
            List<Customer> customers = repository.listCustomers(); // Fetch the list of customers from the repository
            if (customers.isEmpty()) {
                System.out.println("No customers found.");
                return;
            }
            customers.forEach(c -> System.out.printf("ID: %d | %s %s | Email: %s | Phone: %s\n",
                    c.getCustomerID(), c.getFirstName(), c.getLastName(), c.getEmail(), c.getPhoneNumber()));
        } catch (Exception e) {
            System.err.println("Error listing customers: " + e.getMessage());
        }
    }

    private static void findCustomerById() throws CustomerNotFoundException {
        System.out.print("Enter Customer ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Customer c = repository.findCustomerById(id);
        System.out.printf("Found: ID: %d | %s %s | Email: %s | Phone: %s\n",
                c.getCustomerID(), c.getFirstName(), c.getLastName(), c.getEmail(), c.getPhoneNumber());
    }

    private static void createLease() throws Exception {
        System.out.print("Customer ID: ");
        int customerId = Integer.parseInt(scanner.nextLine());
        System.out.print("Vehicle ID: ");
        int vehicleId = Integer.parseInt(scanner.nextLine());
        System.out.print("Start Date (yyyy-MM-dd): ");
        Date startDate = parseDate(scanner.nextLine());
        System.out.print("End Date (yyyy-MM-dd): ");
        Date endDate = parseDate(scanner.nextLine());

        Lease lease = repository.createLease(customerId, vehicleId, startDate, endDate);
        System.out.printf("Lease created! Lease ID: %d\n", lease.getLeaseID());
    }

    private static void returnCar() throws LeaseNotFoundException {
        System.out.print("Lease ID: ");
        int leaseId = Integer.parseInt(scanner.nextLine());
        repository.returnCar(leaseId);
        System.out.println("Car returned successfully.");
    }

    private static void listActiveLeases() {
        List<Lease> leases = repository.listActiveLeases();
        if (leases.isEmpty()) {
            System.out.println("No active leases.");
            return;
        }
        leases.forEach(l -> System.out.printf("Lease ID: %d | Customer ID: %d | Vehicle ID: %d | Start: %s | End: %s | Type: %s\n",
                l.getLeaseID(), l.getCustomerID(), l.getVehicleID(), formatDate(l.getStartDate()), formatDate(l.getEndDate()), l.getType()));
    }

    private static void listLeaseHistory() {
        List<Lease> leases = repository.listLeaseHistory(); // Fixed this
        if (leases.isEmpty()) {
            System.out.println("No lease history.");
            return;
        }
        leases.forEach(l -> System.out.printf("Lease ID: %d | Customer ID: %d | Vehicle ID: %d | Start: %s | End: %s | Type: %s\n",
                l.getLeaseID(), l.getCustomerID(), l.getVehicleID(), formatDate(l.getStartDate()), formatDate(l.getEndDate()), l.getType()));
    }

    private static void recordPayment() throws LeaseNotFoundException {
        System.out.print("Lease ID: ");
        int leaseId = Integer.parseInt(scanner.nextLine());
        Lease lease = repository.listLeaseHistory().stream() // Fixed this
                .filter(l -> l.getLeaseID() == leaseId)
                .findFirst()
                .orElseThrow(() -> new LeaseNotFoundException("Lease ID not found."));
        System.out.print("Payment Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        repository.recordPayment(lease, amount);
        System.out.println("Payment recorded.");
    }

    // Utility
    private static Date parseDate(String input) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(input);
    }

    private static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
