package SnakeIO;

public class Timer {
    private final int length;
    private long lastTick;

    public Timer() {
        this.length = 100;
        this.lastTick = System.currentTimeMillis();
    }

    public boolean timeout() {
        long var1 = System.currentTimeMillis();
        if (var1 > this.lastTick + (long)this.length) {
            this.lastTick += (long)this.length;
            return true;
        } else {
            return false;
        }
    }

    public void mark() {
        this.lastTick = System.currentTimeMillis();
    }
}
