package SnakeIO;

public class Timer {
    private int length;
    private long lastTick;

    public Timer(int length) {
        this.length = length;
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

    public void setLength(int length) {
        this.length = length;
        this.lastTick = System.currentTimeMillis();
    }
}
