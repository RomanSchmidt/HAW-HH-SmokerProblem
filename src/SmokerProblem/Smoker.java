package SmokerProblem;

public class Smoker extends Thread {
    private final Ingredient _ingredient;
    private final Table _table;
    private Cigarette _cigarette;

    /**
     * Eine Referenz zum Tisch ist notwendig um die Zigarette zu nehmen und am Tisch bescheid zu geben, wenn man wieder
     * bereit ist.
     */
    Smoker(String name, Ingredient ingredient, Table table) {
        this.setName(name);
        this._ingredient = ingredient;
        this._table = table;
    }

    /**
     * die eine Zutat, die der Smoker hat
     */
    public Ingredient getIngredient() {
        return this._ingredient;
    }

    /**
     * Der Smoker nimmt die Zigarette, sie zu rauchen und danach meldet er am Tisch, dass er fertig ist.
     */
    public void run() {
        try {
            while (!this.isInterrupted()) {
                this._cigarette = this._table.takeCigarette(this);
                this._smoke();
                this._table.smokerDone();
            }
        } catch (InterruptedException ex) {
            System.err.println("\t" + this + " interrupted!");
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
        System.err.println("\t" + this + ": start rolling");
        this._cigarette.roll();
        System.err.println("\t" + this + ": stop rolling");
        System.err.println("\t" + this + ": start smoking");
        this._cigarette.smoke();
        System.err.println("\t" + this + ": stop smoking");
        this._cigarette = null;
    }
}
