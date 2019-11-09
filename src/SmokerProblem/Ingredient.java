package SmokerProblem;

import java.util.List;
import java.util.Random;

public enum Ingredient {
    Tobacco("Tobacco"),
    Paper("Paper"),
    Matches("Matches");

    private static final List<Ingredient> VALUES = List.of(values());
    public static final int size = VALUES.size();
    private static final Random RANDOM = new Random();
    private final String _name;

    Ingredient(String name) {
        this._name = name;
    }

    public static Ingredient randomIngredient() {
        return VALUES.get(RANDOM.nextInt(size));
    }

    public String getName() {
        return this._name;
    }
}
