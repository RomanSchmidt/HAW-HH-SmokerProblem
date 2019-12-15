package SmokerProblem;

import java.util.ArrayList;

/**
 * Die Table Klasse ist der Hauptknoten für alle parallel laufenden Threads um diese zu steuern und ihnen zu "lauschen".
 * Die Agenten, Smoker, Spoilsport und die Zigarette werden hier behandelt.
 */
public class Table {
    private static final int _smokingMS = 100000;

    private final ArrayList<Agent> _agents = new ArrayList<>();
    private final ArrayList<Smoker> _smoker = new ArrayList<>();
    private Cigarette _cigarette = null;

    Table() {
        this._init();
    }

    /**
     * Die Agenten, Smoker und der Spoilsport werden hier initialisiert und gestartet.
     */
    private void _init() {
        this._agents.add(new Agent("Agent1", this));
        this._agents.add(new Agent("Agent2", this));

        this._smoker.add(new Smoker("PapSmoker", Ingredient.Paper, this));
        this._smoker.add(new Smoker("MatchSmoker", Ingredient.Matches, this));
        this._smoker.add(new Smoker("TobaccoSmoker", Ingredient.Tobacco, this));

        this._smoker.forEach(Thread::start);
        this._agents.forEach(Thread::start);

        Spoilsport spoilsport = new Spoilsport(Table._smokingMS, this);
        spoilsport.start();
    }

    /**
     * Jemand fügt der Zigarette eine Zutat hinzu.
     * Wenn sie fertig ist um genommen zu werden werden die Smoker geweckt.
     */
    public synchronized void addIngredientsToCigarette(Agent agent) throws InterruptedException {
        while (this._cigarette != null) {
            this.wait();
        }
        this._cigarette = new Cigarette();
        Ingredient missingIngredient = Ingredient.randomIngredient();
        for (int j = 0; j < Ingredient.size; ++j) {
            Ingredient currentIngredient = Ingredient.values()[j];
            if (currentIngredient != missingIngredient) {
                System.err.println(agent.getName() + ": adding Ingredient: " + currentIngredient.getName());
                if (this._cigarette.addIngredient(currentIngredient) && this._cigarette.isReadyToBeTaken()) {
                    break;
                }
            }
        }
        notifyAll();
    }

    /**
     * Hier wird sichergestellt, dass nur ein einziger Vorgang möglich ist um die Zigarette nehmen zu können,
     * falls möglich
     */
    public synchronized Cigarette takeCigarette(Smoker smoker) throws InterruptedException {
        while (this._cigarette == null || this._cigarette.getMissingIngredient() != smoker.getIngredient()) {
            this.wait();
        }
        System.err.println("\t" + smoker + ": taking cigarette.");
        Cigarette returnCigarette = this._cigarette;
        this._cigarette = null;
        return returnCigarette;
    }

    /**
     * Der Smoker meldet sich bei den Agenten, dass er fertig ist.
     */
    public synchronized void smokerDone() {
        notifyAll();
    }

    /**
     * Der Spielverderber meldet sich. Alle threads werden interrupted, das Spiel ist vorbei.
     */
    public void timeIsOver() {
        this._agents.forEach(Thread::interrupt);
        this._smoker.forEach(Thread::interrupt);
        System.err.println("all done");
    }
}
