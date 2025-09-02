public class Ingredient {
    private String name;
    private double quantity;
    private String unit;

    public Ingredient(String name, double quantity) {
        this.name = name;
        this.quantity = quantity;
        this.unit = "units";
    }

    public Ingredient(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void updateQuantity(double amount) {
        this.quantity += amount;
        System.out.println(name + " quantity updated by " + amount);
    }

    @Override
    public String toString() {
        return name + "," + quantity + "," + unit;
    }

    // Simple fromString method for basic format
    public static Ingredient fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 2) {
            try {
                String name = parts[0];
                double quantity = Double.parseDouble(parts[1]);
                String unit = parts.length > 2 ? parts[2] : "units";
                return new Ingredient(name, quantity, unit);
            } catch (Exception e) {
                System.out.println("Error parsing ingredient: " + line);
            }
        }
        return null;
    }
}