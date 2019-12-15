package SmokerProblem;

public class Agent extends Thread {
    private final Table _table;

    Agent(String name, Table table) {
        this._table = table;
        this.setName(name);
    }

    public void run() {
        try {
            while(!isInterrupted()) {
                this._table.addIngredientsToCigarette(this);
            }
        } catch (InterruptedException ex) {
            System.err.println(this.getName() + " interrupted!");
        }
    }
}
