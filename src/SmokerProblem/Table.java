package SmokerProblem;

import java.util.ArrayList;

public class Table {
    private final int _smokingMS = 100000;

    private final ArrayList<Agent> _agents = new ArrayList<>();
    private final ArrayList<Smoker> _smoker = new ArrayList<>();
    private Cigarette _cigarette = null;

    Table() {
        this._init();
    }

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

    public void addIngredient(Ingredient ingredient) {
        if (this._cigarette.addIngredient(ingredient) && this._cigarette.isReadyToBeTaken()) {
            this._smoker.forEach(Smoker::wake);
        }
    }

    public synchronized boolean putCigarette() {
        if (this._cigarette == null) {
            this._cigarette = new Cigarette();
            return true;
        }
        return false;
    }

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

    public void smokerDone() {
        this._agents.forEach(Agent::wake);
    }

    public void timeIsOver() {
        this._agents.forEach(Thread::interrupt);
        this._smoker.forEach(Thread::interrupt);
        System.out.println("all done");
    }
}
