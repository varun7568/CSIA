import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        /*System.out.println("SEQUENCE: main started");
        HomeScreen gui = new HomeScreen();
        System.out.println("SEQUENCE: main finished");


         */
        OrderManager om = new OrderManager();
        CustomerManager cm = new CustomerManager();
        Recipes recipes = new Recipes();
        om.loadOrders(cm, recipes); // Load saved data if any

        new ReportsScreen(om);
        }
    }

    //prevent duplicate entries