package SmokerProblem;

/**
 * Dies ist ein unabhängiger Thread der für x sekunden schläft und sich danach am Tisch meldet.
 */
public class Spoilsport extends Thread {
    private final Table _table;
    private final long _waitTime;

    /**
     * - Die Zeit, bis sich dieses Object melden soll
     * - Eine Referenz auf den Tisch, damit sich der Spoilspot melden kann
     */
    Spoilsport(int waitTime, Table table) {
        this._waitTime = waitTime;
        this._table = table;
    }

    /**
     * Schlafe für x Sekunden und melde dich danach am Tisch.
     */
    public synchronized void run() {
        try {
            Thread.sleep(this._waitTime);
        } catch (InterruptedException ex) {
            // Interrupt aufgetreten --> fertig
            System.err.println(this.getName() + " wurde erfolgreich interrupted!");
        }
        System.out.println("time is over");

        this._table.timeIsOver();
    }
}
