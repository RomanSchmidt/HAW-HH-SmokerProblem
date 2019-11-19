package SmokerProblem;

/**
 * Alle Raucher versuchen eine Zigarette vom Tisch zu nehmen, wenn Ihre Zutat die jenige ist, die in der Zigarette fehlt.
 * Wenn die Zigarette da ist, kann ihr die Zutat hinzugefügt werden, sie wird danach gerollt und geraucht.
 * Dem Tisch wird dann bescheid gegeben, wenn der Rauchvorgang beendet ist.
 */
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

    /**
     * Der Smoker versucht eine Zigarette zu nehmen. Danach meldet er am Tisch, dass er fertig ist.
     */
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

    /**
     * Wenn der Versuch die Zigarette zu nehmen erfolglos war, wird geschlafen, bis man wieder geweckt wird.
     * Wenn man eine Zigarette hat, wird geraucht.
     * Diese Methode darf nicht synchronized sein, da das Nehmen parallel passieren darf.
     */
    private void _take() throws InterruptedException {
        this._cigarette = this._table.takeCigarette(this._ingredient);
        if (this._cigarette != null) {
            System.out.println(this.getName() + ": taking cigarette.");
            this._smoke();
            this._cigarette = null;
        } else {
            this._pause();
        }
    }

    /**
     * Auslagerung des Schlafvorgang um der aufrufenden Methode die Möglichkeit zu geben nicht synchronized zu sein.
     */
    public synchronized void _pause() throws InterruptedException {
        this.wait();
    }

    /**
     * Thread wird extern geweckt.
     */
    public synchronized void wake() {
        this.notify();
    }

    /**
     * Das Rauchen ist nicht blockierend für andere Raucher.
     * - Es wird die Zutat hinzugefügt
     * - Die Zigarette wird gerollt
     * - Die Zigarette wird geraucht
     */
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
