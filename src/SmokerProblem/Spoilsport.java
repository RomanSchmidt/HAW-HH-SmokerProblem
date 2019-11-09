package SmokerProblem;

public class Spoilsport extends Thread {
    private final Table _table;
    private final long _waitTime;

    Spoilsport(int waitTime, Table table) {
        this._waitTime = waitTime;
        this._table = table;
    }

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
