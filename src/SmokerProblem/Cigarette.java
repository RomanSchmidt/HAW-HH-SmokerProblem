package SmokerProblem;

import java.util.HashSet;
import java.util.Random;

/**
 * Die Zigarette ist ein Kontainer für die Zutaten.
 * - Sie blockt das befüllen.
 * - Sie hält den Thread auf, wenn sie geraucht und gerollt wird.
 * - Sie stellt sicher, dass zuverlässig erkannt wird, ob Zutaten fehlen, ohne, dass jemand da zwischen etwas
 *   hinzufügen kann.
 */
public class Cigarette {
    private final Random _rand = new Random();
    private final HashSet<Ingredient> _ingredients = new HashSet<>();

    public boolean addIngredient(Ingredient ingredient) {
        if(this._ingredients.size() == 3) {
            return false;
        }
        this._ingredients.add(ingredient);
        return true;
    }

    public boolean isReadyToBeTaken() {
        return this._ingredients.size() == 2;
    }

    public void roll() throws InterruptedException {
        Thread.sleep((1 + this._rand.nextInt(5)) * 1000);
    }

    public void smoke() throws InterruptedException {
        Thread.sleep((1 + this._rand.nextInt(5)) * 1000);
        this._ingredients.clear();
    }

    public Ingredient getMissingIngredient() {
        if (this.isReadyToBeTaken()) {
            for (int i = 0; i < Ingredient.size; ++i) {
                Ingredient ingredient = Ingredient.values()[i];
                if (!this._ingredients.contains(ingredient)) {
                    return ingredient;
                }
            }
        }
        return null;
    }
}
