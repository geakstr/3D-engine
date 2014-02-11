package me.geakstr.engine.core;

import me.geakstr.engine.Main;

public class Timing {

	public static final long SECOND = 1000000000L;
	public static final double frameTime = 1.0 / Main.FRAME_CAP * 1.0;
	private static double delta = frameTime;

	public static long getTime() {
		return System.nanoTime();
	}

	public static double getDelta() {
		return delta;
	}

	public static void setDelta(double delta) {
		Timing.delta = delta;
	}
}