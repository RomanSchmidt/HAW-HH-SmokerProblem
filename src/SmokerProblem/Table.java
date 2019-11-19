package SmokerProblem;

import java.util.ArrayList;

/**
 * Die Table Klasse ist der Hauptknoten für alle parallel laufenden Threads um diese zu steuern und ihnen zu "lauschen".
 * Die Agenten, Smoker, Spoilsport und die Zigarette werden hier behandelt.
 */
public class Table {
    private final int _smokingMS = 100000;

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

        Spoilsport spoilsport = new Spoilsport(this._smokingMS, this);
        spoilsport.start();
    }

    /**
     * Jemand fügt der Zigarette eine Zutat hinzu.
     * Wenn sie fertig ist um genommen zu werden werden die Smoker geweckt.
     */
    public void addIngredient(Ingredient ingredient) {
        if (this._cigarette.addIngredient(ingredient) && this._cigarette.isReadyToBeTaken()) {
            this._smoker.forEach(Smoker::wake);
        }
    }

    /**
     * Hier wird sichergestellt, dass nur ein einziger Vorgang möglich ist um die Zigarette hinlegen zu können,
     * falls möglich
     */
    public synchronized boolean putCigarette() {
        if (this._cigarette == null) {
            this._cigarette = new Cigarette();
            return true;
        }
        return false;
    }

    /**
     * Hier wird sichergestellt, dass nur ein einziger Vorgang möglich ist um die Zigarette nehmen zu können,
     * falls möglich
     */
    public synchronized Cigarette takeCigarette(Ingredient ingredient) {
        if (this._cigarette == null) {
            return null;
        }
        if (this._cigarette.getMissingIngredient() == ingredient) {
            Cigarette returnCigarette = this._cigarette;
            this._cigarette = null;
            return returnCigarette;
        }
        return null;
    }

    /**
     * Der Smoker meldet sich bei den Agenten, dass er fertig ist.
     */
    public void smokerDone() {
        this._agents.forEach(Agent::wake);
    }

    /**
     * Der Spielverderber meldet sich. Alle threads werden interrupted, das Spiel ist vorbei.
     */
    public void timeIsOver() {
        this._agents.forEach(Thread::interrupt);
        this._smoker.forEach(Thread::interrupt);
        System.out.println("all done");
    }
}
