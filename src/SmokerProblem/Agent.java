package SmokerProblem;

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
            while(!this.isInterrupted()) {
                this._table.addIngredientsToCigarette(this);
            }
        } catch (InterruptedException ex) {
            System.err.println(this.getName() + " interrupted!");
        }
    }
}
