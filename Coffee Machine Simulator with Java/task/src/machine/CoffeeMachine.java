package machine;
import java.util.Scanner;

public class CoffeeMachine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Supplies supplies = new Supplies(400, 540, 120, 9);
        Machine machine = new Machine(supplies, 550, 0);
        // Loop to continuously process actions until "exit" is entered
        while (true) {
            // Request user action
            System.out.print("Write action (buy, fill, take, clean, remaining, exit): \n> ");
            String action = scanner.nextLine().toLowerCase();

            if (action.equals("exit")) {
                break;  // Exit the loop and end the program
            }

            switch (action) {
                case "buy":
                    if (machine.needsClean()) {
                        System.out.println("I need cleaning!");
                        break;
                    }
                    System.out.print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: \n> ");
                    String choice = scanner.nextLine();

                    machine.buy(choice);
                    break;

                case "fill":
                    // Get the amounts to fill the supplies
                    System.out.println("Write how many ml of water you want to add: \n> ");
                    int water = scanner.nextInt();
                    System.out.println("Write how many ml of milk you want to add: \n> ");
                    int milk = scanner.nextInt();
                    System.out.println("Write how many grams of coffee beans you want to add: \n> ");
                    int coffeeBeans = scanner.nextInt();
                    System.out.println("Write how many disposable cups you want to add: \n> ");
                    int cups = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    machine.fill(water, milk, coffeeBeans, cups);
                    break;

                case "take":
                    machine.take();
                    break;
                case "remaining":
                    Report report = machine.report();
                    System.out.println(report.report());
                    break;
                case "clean":
                    machine.cleanMachine();
                default:
                    System.out.println("Invalid action. Please try again.");
                    break;
            }
        }

        scanner.close();
    }

}

class Supplies {
    private int water;
    private int milk;
    private int beans;
    private int cups;

    public Supplies(int water, int milk, int beans, int cups){
        this.water = water;
        this.milk = milk;
        this.beans = beans;
        this.cups = cups;
    }

    public void addWater(int amount) {
        this.water += amount;
    }

    public void addMilk(int amount) {
        this.milk += amount;
    }

    public void addBeans(int amount) {
        this.beans += amount;
    }

    public void addCups(int amount) {
        this.cups += amount;
    }

    public int water() {
        return water;
    }

    public int milk() {
        return milk;
    }

    public int beans() {
        return beans;
    }

    public int cups() {
        return cups;
    }

    public boolean enough(CoffeeType type) {
        if (water < type.getWater()) {
            System.out.println("Sorry, not enough water!");
            return false;
        }
        if (milk < type.getMilk()) {
            System.out.println("Sorry, not enough milk!");
            return false;
        }
        if (beans < type.getBeans()) {
            System.out.println("Sorry, not enough coffee beans!");
            return false;
        }
        if (cups < type.getCups()) {
            System.out.println("Sorry, not enough cups!");
            return false;
        }

        return true;
    }
}

class Machine{

    private static final int MAX_CUPS_BEFORE_CLEAN = 10;
    private int money;
    private Supplies supplies;
    private int cupsMade;

    public Machine(Supplies supplies, int money, int cupsMade) {
        this.supplies = supplies;
        this.money = money;
        this.cupsMade = cupsMade;
    }

    public void buy(String choice) {
        switch (choice) {
            case "1" -> this.buy(CoffeeType.ESPRESSO);
            case "2" -> this.buy(CoffeeType.LATTE);
            case "3" -> this.buy(CoffeeType.CAPPUCCINO);
            case "back" -> {
            }
        }
    }

    public boolean needsClean() {
        return cupsMade >= MAX_CUPS_BEFORE_CLEAN;
    }

    public void buy(CoffeeType type) {
        if(!supplies.enough(type)) {
            return;
        }

        // Deduct resources if there are enough
        supplies = new Supplies(
                supplies.water() - type.getWater(),
                supplies.milk() - type.getMilk(),
                supplies.beans() - type.getBeans(),
                supplies.cups() - type.getCups()
        );

        this.money += type.getCost();
        this.cupsMade += 1;

        System.out.println("I have enough resources, making you a coffee!");
    }

    public void fill(int water, int milk, int coffeeBeans, int cups) {
        supplies.addWater(water);
        supplies.addMilk(milk);
        supplies.addBeans(coffeeBeans);
        supplies.addCups(cups);
    }

    public void take() {
        this.money = 0;
    }

    public Report report() {
        return new Report(this.supplies, this.money);
    }

    public void cleanMachine() {
        if (cupsMade >= MAX_CUPS_BEFORE_CLEAN) {
            cupsMade = 0;
            System.out.println("I have been cleaned!");
        }
    }
}

class Report {
    private final Supplies supplies;
    private final int money;

    public Report(Supplies supplies, int money) {
        this.supplies = supplies;
        this.money = money;
    }

    public String report() {
        return "The coffee machine has:\n" +
                this.supplies.water() + " ml of water\n" +
                this.supplies.milk() + " ml of milk\n" +
                this.supplies.beans() + " g of coffee beans\n" +
                this.supplies.cups() + " disposable cups\n" +
                "$" + this.money + " of money";
    }
}

enum CoffeeType {
    ESPRESSO(250, 0, 16, 1, 4),
    LATTE(350, 75, 20, 1, 7),
    CAPPUCCINO(200, 100, 12, 1, 6);

    private final int water;
    private final int milk;
    private final int beans;
    private final int cups;
    private final int cost;

    CoffeeType(int water, int milk, int beans, int cups, int cost) {
        this.water = water;
        this.milk = milk;
        this.beans = beans;
        this.cups = cups;
        this.cost = cost;
    }

    public int getWater() {
        return water;
    }

    public int getMilk() {
        return milk;
    }

    public int getBeans() {
        return beans;
    }

    public int getCups() {
        return cups;
    }

    public int getCost() {
        return cost;
    }
}
