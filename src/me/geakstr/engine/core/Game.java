package me.geakstr.engine.core;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

public class Game implements Runnable {
    private static Game instance;


    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    public static final boolean VSYNC_ENABLED = false;
    public static final boolean RESIZE_ENABLED = false;
    public static final boolean FULLSCREEN_ENABLED = false;
    public static final int FRAME_CAP = 60;

    public static String RES_DIR;

    public Game(String resDir) {
        RES_DIR = resDir;
        instance = this;
        try {
            Display.create();
            Window.setDisplayMode(WIDTH, HEIGHT, FULLSCREEN_ENABLED);
            run();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    long lastFrame, lastFPS;
    int fps;

    public void run() {
        init();
        getDelta();
        lastFPS = getTime();
        while (!Display.isCloseRequested()) {
            update(getDelta());
            render();
            Display.update();
            if (Window.wasResized()) Window.resized();
            Display.sync(FRAME_CAP);
        }
        end();
    }

    public int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }

    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    public void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("3D game engine [FPS: " + fps + "]");
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }

    public static void end() {
        instance.dispose();
        instance = null;
        Display.destroy();
        System.exit(0);
    }

    public void init() {
        Display.setVSyncEnabled(VSYNC_ENABLED);
        Display.setResizable(RESIZE_ENABLED);
    }

    public void update(int delta) {
    }

    public void render() {
    }

    public void dispose() {
    }
}
