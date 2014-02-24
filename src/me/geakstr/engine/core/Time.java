package me.geakstr.engine.core;

public class Time {

    public static final long SECOND = 1000000000L;
    public static final double frameTime = 1.0 / Game.FRAME_CAP * 1.0;
    private static double delta = frameTime;

    public static long getTime() {
        return System.nanoTime();
    }

    public static double getDelta() {
        return delta;
    }

    public static void setDelta(double delta) {
        Time.delta = delta;
    }
}