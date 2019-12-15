package SmokerProblem;

public class Smoker extends Thread {
    private final Ingredient _ingredient;
    private final Table _table;
    private Cigarette _cigarette;

    Smoker(String name, Ingredient ingredient, Table table) {
        this.setName(name);
        this._ingredient = ingredient;
        this._table = table;
    }

    public Ingredient getIngredient() {
        return this._ingredient;
    }

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
