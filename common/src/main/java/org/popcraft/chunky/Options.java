package org.popcraft.chunky;

public class Options {
    private boolean isSilent;
    private int quietInterval = 1;
    private int maxWorking = 2048;

    public boolean isSilent() {
        return this.isSilent;
    }

    public void setSilent(boolean silent) {
        this.isSilent = silent;
    }

    public int getQuietInterval() {
        return this.quietInterval;
    }

    public void setQuietInterval(int quietInterval) {
        this.quietInterval = quietInterval;
    }

    public int getMaxWorking() {
        return this.maxWorking;
    }

    public void setMaxWorking(int maxWorking) {
        this.maxWorking = maxWorking;
    }
}
