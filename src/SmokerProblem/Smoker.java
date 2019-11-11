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

    public void run() {
        try {
            while (true) {
                this._take();
                this._table.smokerDone();
            }
        } catch (InterruptedException ex) {
            System.err.println(this.getName() + " interrupted!");
        }
    }

    private void _take() throws InterruptedException {
        this._cigarette = this._table.takeCigarette(this._ingredient);
        if (this._cigarette != null) {
            System.out.println(this.getName()+": taking cigarette.");
            this._smoke();
            this._cigarette = null;
        } else {
            this._pause();
        }
    }

    public synchronized void _pause() throws InterruptedException {
        this.wait();
    }

    public synchronized void wake() {
        this.notify();
    }

    private void _smoke() throws InterruptedException {
        this._cigarette.addIngredient(this._ingredient);
        System.out.println(this.getName() + ": start rolling");
        this._cigarette.roll();
        System.out.println(this.getName() + ": stop rolling");
        System.out.println(this.getName() + ": start smoking");
        this._cigarette.smoke();
        System.out.println(this.getName() + ": stop smoking");
    }
}
