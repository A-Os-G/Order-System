import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Vector;

abstract class Person {
    private String name, phone;

    public Person() {
    }

    public Person(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}

class Customer extends Person {
    private Order order;
    private PostOrder post;

    public Customer(String name, String phone) {
        super(name, phone);
        order = new Order();
        post = new PostOrder();
    }

    public Order getOrder() {
        return order;
    }

    public void addItem(Items item) {
        order.addItem(item);
    }

    public void displayOrder() {
        System.out.printf("Order details for customer (%s):\n", getName());
        order.display();
    }

    public PostOrder getPost() {
        return post;
    }

    public double calculateTotal() {
        return post.calculatetotal(order);
    }
}

class Staff extends Person {
    protected Order o;
    private String StaffID;

    public Staff() {
    }

    public Staff(String name, String phone, String id) {
        super(name, phone);
        StaffID = id;
    }

    public String getID() {
        return StaffID;
    }

    public void assignOrder(Order o) {
        this.o = o;
    }

    public Order getOrder(){
        return o;
    }

    public void deleteItem(int i) {
        o.deleteItem(i);
    }

    public void displayOrder() {
        System.out.println("Order details: ");
        o.display();
    }
}

class Chef extends Staff {
    public Chef() {
    }

    public Chef(String name, String phone, String id) {
        super(name, phone, id);
    }

    public void changeStatus() {
        Scanner inp = new Scanner(System.in);
        System.out.println("Hello Chef " + super.getName());
        System.out.println("You have an order with id: " + o.getOrderID());

        System.out.println("\n\nPlease update the status as follows: ");
        String status = "";
        System.out.println("[1] Accepted");
        System.out.println("[2] Cooking");
        System.out.println("[3] Ready");

        System.out.print("Your answer => ");

        int choice = inp.nextInt();

        boolean loop = true;
        while (loop) {
            switch (choice) {
                case 1:
                    o.setStatus("Accepted");
                    loop = false;
                    break;

                case 2:
                    o.setStatus("Cooking");
                    loop = false;
                    break;
                case 3:
                    o.setStatus("Ready");
                    loop = false;
                    break;

                default:
                    System.out.print("You entered an invalid option, please choose 1-3: ");
                    loop = false;
                    break;
            }
        }

    }

    public void displayOrder() {
        System.out.println("Orders Assigned for Chef: " + getName());
        super.displayOrder();
    }
}

class Waiter extends Staff {
    public Waiter() {
    }

    public Waiter(String name, String phone, String id) {
        super(name, phone, id);
    }

    public void displayOrder(Vector<Customer> customers) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("\n");
        for (int i = 0; i < customers.size(); i++){
            System.out.println("-----------------------------------------------");
            System.out.println("-----------------------------------------------");
            System.out.printf("Order [%d]:\n\n", (i+1));
            customers.elementAt(i).displayOrder();
            System.out.println("\n");
        }
    }

    public void assignOrder(Vector<Customer> customers, Vector<Staff> staffs){
        Scanner inp = new Scanner(System.in);
        int num;
        while(true){
            System.out.println("Which order you want to assign to a chef? choose from 1 to " + customers.size());
            System.out.print("Your answer => ");
            num = inp.nextInt();
            num--;
            if(num >= 0 && num < customers.size())
                break;
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("To which chief u want to assign?");
        Vector<Chef> tmp = new Vector<Chef>();
        for(int i = 0; i < staffs.size(); i++){
            if(staffs.elementAt(i) instanceof Chef){
                tmp.add((Chef)staffs.elementAt(i));
            }
        }
        for(int i = 0; i < tmp.size(); i++){
            System.out.printf("Chef [%d]: ", (i+1));
            System.out.println(tmp.elementAt(i).getName());
        }
        int num1;
        while(true){
            System.out.print("\n\nChoose a Chef: ");
            num1 = inp.nextInt();
            num1--;
            if(num1 >= 0 && num1 < tmp.size()){
                tmp.elementAt(num1).assignOrder(customers.elementAt(num).getOrder());
                break;
            }
        }
        for(int i = 0; i < staffs.size(); i++){
            if(staffs.elementAt(i).getName().equals(tmp.elementAt(num1).getName())){
                staffs.elementAt(i).assignOrder(tmp.elementAt(num1).getOrder());
            }
        }
        System.out.println("\n\nOrder assigned successfully!\n\n");
    }
}

class Items {
    private String name;
    private double price;

    public Items(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void displayInfo() {
        System.out.println("Item Name: " + name);
        System.out.println("Item Price: " + price);
    }

}

class Table {
    private int tableID;
    private Order order;

    public Table(int tableID) {
        this.tableID = tableID;
        order = null;
    }

    public void assignOrder(Order order) {
        this.order = order;
    }

    public void viewtableOrder() {
        System.out.printf("Order details for table No: \n" + tableID);
        order.display();
    }

    public int getId() {
        return tableID;
    }

    public Order getOrder() {
        return order;
    }

    public boolean getStatus() {
        if (order == null) {
            return true;
        } else {
            return false;
        }
    }

}

class PostOrder {
    private double totalprice;
    private String feedback;
    private int rating;

    public PostOrder() {
        totalprice = 0;
        feedback = "";
        rating = 0;
    }

    public double gettotalprice() {
        return totalprice;
    }

    public int getrating() {
        return rating;
    }

    public String getfeedback() {
        return feedback;
    }

    public double calculatetotal(Order order) {
        double total = 0;
        for (int i = 0; i < order.getItems().size(); i++) {
            total += order.getItems().elementAt(i).getPrice();
        }
        return total;
    }

    public void setfeedback(Scanner input1) {
        System.out.print("Please type any comment about the system: ");
        feedback = input1.nextLine();
        input1.close();
    }

    public void setrating() {
        Scanner inp = new Scanner(System.in);
        while (true) {
            System.out.print("Please rate your experience from 1 (worst) to 5 (best): ");
            rating = inp.nextInt();
            if (rating > 0 && rating < 6) {
                break;
            }

        }
        inp.nextLine();
        setfeedback(inp);
    }
}

class Order {
    private static int orderCounter = 1001; // starting number
    private int orderID;
    private String status;
    private Vector<Items> item;

    public Order() {
        orderID = orderCounter;
        orderCounter++;
        status = "Accepted";
        item = new Vector<Items>();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public int getOrderID() {
        return orderID;
    }

    public Vector<Items> getItems() {
        return item;
    }

    public void addItem(Items a) {
        item.add(a);
    }

    public void display() {
        System.out.printf("\nOrder ID: %d", orderID);
        System.out.println("\nOrder status: " + getStatus());

        for (int i = 0; i < item.size(); i++) {
            System.out.printf("\nItem [%d]\n", i + 1);
            item.get(i).displayInfo();
            ;
        }
    }

    public void deleteItem(int i) {
        item.remove(i);
    }
}

public class Project {
    public static void main(String[] args) throws Exception {

        Scanner input = new Scanner(System.in);
        Items[] items = {
                new Items("Kbsa", 12.99),
                new Items("Maqloupa", 14.99),
                new Items("Nasi Lemak", 8.99),
                new Items("Hanez Chicken", 11.99),
                new Items("Hanez Lamb", 19.99),
                new Items("Ayam Georeng", 14.99),
                new Items("Arabic Tea", 4.99)

        };

        Table[] tables = {
                new Table(1),
                new Table(2),
                new Table(3),
                new Table(4),
                new Table(5),
                new Table(6)
        };

        Vector<Customer> customers = new Vector<Customer>();

        customers.add(new Customer("Moataz", "+601139969137"));
        customers.add(new Customer("Mohammed", "+966509473092"));
        customers.add(new Customer("Yousef", "+601139969138"));

        Vector<Staff> staffs = new Vector<Staff>();

        staffs.add(new Chef("Mohammed", "+601139969139", "C111"));
        staffs.add(new Waiter("Moataz", "+601139969140", "W111"));
        staffs.add(new Chef("Yousef", "+601139969142", "C112"));

        customers.elementAt(0).addItem(items[1]);
        customers.elementAt(0).addItem(items[2]);
        customers.elementAt(0).getOrder();
        tables[1].assignOrder(customers.elementAt(0).getOrder());
        customers.elementAt(0).calculateTotal();

        customers.elementAt(1).addItem(items[2]);
        customers.elementAt(1).addItem(items[3]);
        tables[2].assignOrder(customers.elementAt(1).getOrder());

        customers.elementAt(2).addItem(items[2]);
        customers.elementAt(2).addItem(items[3]);
        customers.elementAt(2).addItem(items[0]);
        tables[4].assignOrder(customers.elementAt(2).getOrder());

        staffs.elementAt(0).assignOrder(customers.elementAt(0).getOrder());
        staffs.elementAt(2).assignOrder(customers.elementAt(2).getOrder());

        String l = login(staffs);
        while (true) {
            if (l.charAt(0) == 'C') {// Chef interface

                Chef Cstaff = new Chef();

                for (int i = 0; i < staffs.size(); i++) {
                    if (staffs.elementAt(i).getID().equals(l)) {
                        Cstaff = (Chef) staffs.elementAt(i);

                    }

                }
                int choice;
                Scanner Cinp = new Scanner(System.in);
                System.out.println("\nChoose an option:");
                System.out.println("[1] View Orders");
                System.out.println("[2] Change order status");
                System.out.println("[3] exit");
                System.out.print("Your choice => ");

                choice = Cinp.nextInt();

                if (choice == 1) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    Cstaff.displayOrder();
                } else if (choice == 2) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    Cstaff.changeStatus();
                    System.out.println("\nOrder Status has been changed successfully!\n\n");
                } else {
                    System.out.println("Bye Bye :)");
                    System.exit(0);
                }

            } else if (l.charAt(0) == 'W') {//Waiter's interface
                Waiter Wstaff = new Waiter();

                for (int i = 0; i < staffs.size(); i++) {
                    if (staffs.elementAt(i).getID().equals(l)) {
                        Wstaff = (Waiter) staffs.elementAt(i);
                    }
                }
                Scanner Cinp = new Scanner(System.in);

                while(true){
                    System.out.println("\nChoose an option:");
                    System.out.println("[1] View Orders");
                    System.out.println("[2] exit");
                    System.out.print("Your choice => ");
                    int choice;

                    choice = Cinp.nextInt();
                    while(true){
                        if(choice == 1){
                            Wstaff.displayOrder(customers);
                            Wstaff.assignOrder(customers, staffs);
                            break;
                        }
                        else if(choice == 2){
                            System.out.println("Bye Bye :)");
                            System.exit(0);
                            break;
                        }
                        else{
                            System.out.println("Invalid input try again!");
                        }
                    }
                }

            } else {// Customer interface
                String name, phone;
                System.out.print("Please Enter your name: ");
                name = input.nextLine();
                System.out.print("Please Enter your Phone Number: ");
                phone = input.nextLine();
                customers.add(new Customer(name, phone));

                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println("Welcome " + customers.lastElement().getName());
                System.out.println("\nThis is our retaurant Menu:");
                for (int i = 0; i < items.length; i++) {
                    System.out.printf("[%d] %-14s%7.2f", i + 1, items[i].getName(), items[i].getPrice());
                    System.out.println();
                }
                while (true) {
                    System.out.println();
                    System.out.println("Which item you would like to add => ");
                    System.out.print("Your answer => ");
                    int index = input.nextInt();
                    index--;
                    customers.lastElement().addItem(items[index]);

                    System.out.print("\nDo you want to add another item? [Y/N] => ");
                    String a;
                    a = input.next();
                    if (a.equals("N") || a.equals("n")) {
                        break;
                    }
                }

                int ch = 0;
                boolean breakLoop = false;
                System.out.print("\033[H\033[2J");
                System.out.flush();
                while (!breakLoop) {
                    System.out.print("Please choose your table number from 1 to 6 => ");
                    ch = input.nextInt();
                    switch (ch) {
                        case 1:
                            if (!(tables[0].getStatus())) {
                                tables[0].assignOrder(customers.lastElement().getOrder());
                                System.out.println("The table is already reserved... ");
                                break;
                            }
                            breakLoop = true;
                            break;
                        case 2:
                            if (!(tables[1].getStatus())) {
                                tables[1].assignOrder(customers.lastElement().getOrder());
                                System.out.println("The table is already reserved... ");
                                break;
                            }
                            breakLoop = true;
                            break;
                        case 3:
                            if (!(tables[2].getStatus())) {
                                tables[2].assignOrder(customers.lastElement().getOrder());
                                System.out.println("The table is already reserved... ");
                                break;
                            }
                            breakLoop = true;
                            break;
                        case 4:
                            if (!(tables[3].getStatus())) {
                                tables[3].assignOrder(customers.lastElement().getOrder());
                                System.out.println("The table is already reserved... ");
                                break;
                            }
                            breakLoop = true;
                            break;
                        case 5:
                            if (!(tables[4].getStatus())) {
                                tables[4].assignOrder(customers.lastElement().getOrder());
                                System.out.println("The table is already reserved... ");
                                break;
                            }
                            breakLoop = true;
                            break;
                        case 6:
                            if (!(tables[5].getStatus())) {
                                tables[5].assignOrder(customers.lastElement().getOrder());
                                System.out.println("The table is already reserved... ");
                                break;
                            }
                            breakLoop = true;
                            break;
                        default:
                            System.out.print("Wrong input try again!!\n => ");
                            break;
                    }

                }
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println("Table Number: " + tables[(ch - 1)].getId());
                customers.lastElement().displayOrder();
                System.out.println("\n--------------------------------------------------------");
                System.out.println("--------------------------------------------------------");
                System.out.printf("Summary of order [%d]: ", customers.lastElement().getOrder().getOrderID());
                System.out.println("Number of items: " + customers.lastElement().getOrder().getItems().size());
                System.out.println("Total Price : RM" + customers.lastElement().calculateTotal());
                System.out.println("--------------------------------------------------------");
                System.out.println("--------------------------------------------------------\n\n");
                customers.lastElement().getPost().setrating();
                input.close();
                System.out.println("\n\nThank you for using our App!\nWish to enjoy your meal\n\n\n");
                break;
            }
        }

    }

    public static String login(Vector<Staff> staffs) {
        Scanner inp = new Scanner(System.in);
        System.out.println("----------------------------------------------------------------------");
        System.out.println("----------------------------------------------------------------------");
        System.out.println("************Hello and welcome to our Restaurant System************");
        System.out.println("----------------------------------------------------------------------");
        System.out.println("----------------------------------------------------------------------");
        int n;
        String id;
        while (true) {
            try {
                System.out.println("Would you like to sign in to the system as:");
                System.out.println("[1] Customer");
                System.out.println("[2] Staff");
                System.out.print("Your answer => ");
                n = inp.nextInt();
                if (n == 1) {
                    return "c";
                } else {
                    inp.nextLine();
                    System.out.print("\nPlease Enter Your ID: ");
                    id = inp.nextLine();

                    for (int i = 0; i < staffs.size(); i++) {
                        if (staffs.elementAt(i).getID().equals(id.toUpperCase())) {
                            if (id.charAt(0) == 'C') {
                                System.out.println("\n\nWelcome, Chef " + staffs.elementAt(i).getName());
                                return staffs.elementAt(i).getID();
                            } else {
                                System.out.println("\n\nWelcome, Waiter " + staffs.elementAt(i).getName());
                                return staffs.elementAt(i).getID();
                            }

                        }
                    }
                    System.out.println("Sorry You entered an invalid id ");

                }
            } catch (InputMismatchException e) {
                System.out.println("\n\nInvalid input! Please enter an integer.");
                inp.nextLine();
            }
        }

    }
}