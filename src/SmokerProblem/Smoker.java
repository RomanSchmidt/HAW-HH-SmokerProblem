package SmokerProblem;

public class Smoker extends Thread {
    private final Ingredient _ingredient;
    private final Table _table;
    private Cigarette _cigarette;

    /**
     * Eine Referenzu zum Tisch ist notwendig um die Zigarette zu nehmen und am Tisch bescheid zu geben, wenn man wieder
     * bereit ist.
     */
    Smoker(String name, Ingredient ingredient, Table table) {
        this.setName(name);
        this._ingredient = ingredient;
        this._table = table;
    }

    public Ingredient getIngredient() {
        return this._ingredient;
    }

    /**
     * Der Smoker versucht eine Zigarette zu nehmen. Danach meldet er am Tisch, dass er fertig ist.
     */
    public void run() {
        try {
            while (!isInterrupted()) {
                this._cigarette = this._table.takeCigarette(this);
                this._smoke();
                this._table.smokerDone();
            }
        } catch (InterruptedException ex) {
            System.err.println("\t" + this.getName() + " interrupted!");
        }
    }

    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * Das Rauchen ist nicht blockierend für andere Raucher.
     * - Es wird die Zutat hinzugefügt
     * - Die Zigarette wird gerollt
     * - Die Zigarette wird geraucht
     */
    private void _smoke() throws InterruptedException {
        this._cigarette.addIngredient(this._ingredient);
        System.err.println("\t" + this.getName() + ": start rolling");
        this._cigarette.roll();
        System.err.println("\t" + this.getName() + ": stop rolling");
        System.err.println("\t" + this.getName() + ": start smoking");
        this._cigarette.smoke();
        System.err.println("\t" + this.getName() + ": stop smoking");
        this._cigarette = null;
    }
}
