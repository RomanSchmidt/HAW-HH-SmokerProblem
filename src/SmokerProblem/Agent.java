package SmokerProblem;

public class Agent extends Thread {
    private final Table _table;

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


    public synchronized void wake() {
        this.notify();
    }

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
    
    private void _addIngredient(Ingredient currentIngredient) {
        this._table.addIngredient(currentIngredient);
    }
}
