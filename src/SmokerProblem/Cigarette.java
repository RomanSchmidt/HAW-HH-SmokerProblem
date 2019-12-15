package SmokerProblem;

import java.util.HashSet;
import java.util.Random;

/**
 * Die Zigarette ist ein Kontainer für die Zutaten.
 * Sie ermöglicht das Befüllen.
 * Im Grunde ist sie nur ein Kontainer, verbraucht jedoch Zeit, wenn man sie rollt und raucht.
 */
public class Cigarette {
    private final Random _rand = new Random();
    private final HashSet<Ingredient> _ingredients = new HashSet<>();

    /**
     * Wenn es noch keine 3 Zutaten sind, füge sie hinzu.
     * Die Kontrolle, dass keine doppelt sind, überlassen wir anderen.
     */
    public boolean addIngredient(Ingredient ingredient) {
        if(this._ingredients.size() == 3) {
            return false;
        }
        this._ingredients.add(ingredient);
        return true;
    }

    /**
     * fehlt nur noch eine Zutat?
     */
    public boolean isReadyToBeTaken() {
        return this._ingredients.size() == 2;
    }

    /**
     * verbrauche Zeit
     */
    public void roll() throws InterruptedException {
        Thread.sleep((1 + this._rand.nextInt(5)) * 1000);
    }

    /**
     * verbrauche Zeit und leere die Zutaten danach
     */
    public void smoke() throws InterruptedException {
        Thread.sleep((1 + this._rand.nextInt(5)) * 1000);
        this._ingredients.clear();
    }

    /**
     * stelle fest, welche Zutat noch fehlt
     */
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
