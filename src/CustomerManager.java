import java.io.*;
import java.util.ArrayList;
import java.util.function.Function;

public class CustomerManager {
    private ArrayList<Customer> customers;
    private static final String FILE_NAME = "customers.txt";

    public CustomerManager() {
        customers = new ArrayList<>();
        loadCustomers(); // Load customers on initialization
    }

    // Add this method to load customers from file
    public void loadCustomers() {
        customers = FileHandler.loadFromFile(FILE_NAME, line -> {
            String[] parts = line.split(",", 4);
            if (parts.length >= 3) {
                Customer customer = new Customer(parts[0], parts[1], parts[2]);
                if (parts.length == 4) {
                    String[] orderArray = parts[3].split("\\|");
                    for (String order : orderArray) {
                        customer.addOrder(order);
                    }
                }
                return customer;
            }
            return null;
        });
        selectionSortCustomers();
    }

    // Add this method to save customers to file
    public void saveCustomers() {
        FileHandler.saveToFile(FILE_NAME, customers, Customer::toString);
    }

    // Update your existing addCustomer method to call saveCustomers
    public void addCustomer(Customer customer) {
        if (getCustomerByName(customer.getName()) == null) {
            customers.add(customer);
            selectionSortCustomers();
            saveCustomers();
        } else {
            System.out.println("Customer already exists.");
        }
    }

    // Update your existing deleteCustomer method to call saveCustomers
    public void deleteCustomer(String name) {
        Customer customerToRemove = null;
        for (Customer c : customers) {
            if (c.getName().equalsIgnoreCase(name)) {
                customerToRemove = c;
                break;
            }
        }
        if (customerToRemove != null) {
            customers.remove(customerToRemove);
            saveCustomers(); // Save after deleting
            System.out.println("Customer '" + name + "' deleted successfully.");
        } else {
            System.out.println("Customer '" + name + "' not found.");
        }
    }

    // Your existing methods remain the same...
    private void selectionSortCustomers() {
        int n = customers.size();
        for(int i = 0; i < n-1; i++) {
            int minIndex = i;
            for(int j = i+1; j < n; j++) {
                String name1 = customers.get(j).getName().toLowerCase();
                String name2 = customers.get(minIndex).getName().toLowerCase();
                if(name1.compareTo(name2) < 0) {
                    minIndex = j;
                }
            }
            if(minIndex != i) {
                Customer temp = customers.get(i);
                customers.set(i, customers.get(minIndex));
                customers.set(minIndex, temp);
            }
        }
    }

    public int binarySearchCustomer(String target) {
        int left = 0;
        int right = customers.size() - 1;

        while(left <= right) {
            int mid = (left + right) / 2;
            String midName = customers.get(mid).getName();

            int compare = midName.compareToIgnoreCase(target);
            if(compare == 0) {
                return mid;
            } else if(compare < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    public Customer getCustomerByName(String name) {
        for (Customer c : customers) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    public ArrayList<Customer> getAllCustomers() {
        return customers;
    }

    public void showAllCustomers() {
        System.out.println("Existing customers:");
        for(Customer c: customers) {
            System.out.println(c.getName() + " - " + c.getPhoneNum());
        }
    }
}

