package SmokerProblem;

/**
 * Agenten bedienen den Tisch, und legen nach einander die Zutaten hin, die sie haben.
 * Es kann immer nur ein zufällig gewählter Agent gleichzeitig den Tisch bedienen.
 * So bald die Zigarette vom Tisch genommen wurde, wird ein Agent aktiv.
 */
public class Agent extends Thread {
    private final Table _table;

    /**
     * Der Agent muss eine Referenz auf den Tisch haben.
     * Der Name dient dazu die Ausgaben eindeutig einem Thread zu ordnen zu können.
     */
    Agent(String name, Table table) {
        this._table = table;
        this.setName(name);
    }

    public void run() {
        try {
            while(true) {
                this._addIngredientsToCigarette();
            }
        } catch (InterruptedException ex) {
            System.err.println(this.getName() + " interrupted!");
        }
    }

    /**
     * Der Agent wird geweckt.
     */
    public synchronized void wake() {
        this.notify();
    }

    /**
     * Wenn es nicht möglich ist eine Zigarette auf den Tisch zu legen, geht der Agent schlafen, bis er geweckt wirt.
     * Sonst fügt er die Zutaten hinzu, die er hat und ruft eine nicht synchronisierte methode auf um dem Tisch bescheid
     * zu geben, dass er fertig ist.
     */
    private synchronized void _addIngredientsToCigarette() throws InterruptedException {
        while (!this._table.putCigarette()) {
            this.wait();
        }
        Ingredient missingIngredient = Ingredient.randomIngredient();
        for (int j = 0; j < Ingredient.size; ++j) {
            Ingredient currentIngredient = Ingredient.values()[j];
            if (currentIngredient != missingIngredient) {
                System.out.println(this.getName() + ": adding Ingredient: " + currentIngredient.getName());
                this._addIngredient(currentIngredient);
            }
        }
    }

    /**
     * Da die Raucher am Tisch sitzen, wird dem Tisch bescheid gegeben, dass alle Zutaten vorliegen.
     */
    private void _addIngredient(Ingredient currentIngredient) {
        this._table.addIngredient(currentIngredient);
    }
}
