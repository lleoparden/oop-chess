package com.example.oop_chess;

public class Timer {
    private long startTime;
    private long pauseTime;
    private boolean running;
    private boolean paused;
    private long elapsed;

    public Timer() {
        this.running = false;
        this.paused = false;
        this.startTime = 0;
        this.pauseTime = 0;
        this.elapsed = 0;
        start();
    }

    public void start() {
        if (!running) {
            running = true;
            paused = false;
            startTime = System.currentTimeMillis() - pauseTime; // Resume from the paused state
            new Thread(this::run).start();
        }
    }

    public void start(int time) {
        if (!running) {
            running = true;
            paused = false;
            startTime = System.currentTimeMillis() - (pauseTime + (long) time); // Resume from the paused state
            new Thread(this::run).start();
        }
    }

    public synchronized void pause() {
        if (running && !paused) {
            paused = true;
            pauseTime = System.currentTimeMillis() - startTime;
        }
    }

    public synchronized void resume() {
        if (running && paused) {
            paused = false;
            startTime = System.currentTimeMillis() - pauseTime;
        }
    }

    public synchronized void stop() {
        running = false;
        paused = false;
        startTime = 0;
        pauseTime = 0;
        elapsed = 0;
    }

    private void run() {
        while (running) {
            if (!paused) {
                elapsed = ((System.currentTimeMillis() - startTime)/1000);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public long getElapsed() {
        return elapsed;
    }
}
