package me.geakstr.engine.core;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.glViewport;

public class Window {

    private Window() {}

    public static void init(int width, int height, boolean fullscreen) {
        try {
            Display.create();
            setDisplayMode(width, height, fullscreen);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void switchFullscreen() {
        setFullscreen(!Display.isFullscreen());
    }

    public static void setFullscreen(boolean fullscreen) {
        setDisplayMode(Display.getDisplayMode(), fullscreen);
    }

    public static boolean setDisplayMode(DisplayMode mode, boolean fullscreen) {
        return setDisplayMode(mode.getWidth(), mode.getHeight(), fullscreen);
    }

    public static boolean setDisplayMode(DisplayMode mode) {
        return setDisplayMode(mode, false);
    }

    public static boolean setDisplayMode(int width, int height) {
        return setDisplayMode(width, height, false);
    }

    public static boolean setDisplayMode(int width, int height, boolean fullscreen) {
        if ((Display.getDisplayMode().getWidth() == width) && (Display.getDisplayMode().getHeight() == height) && (Display.isFullscreen() == fullscreen))
            return true;
        try {
            DisplayMode targetDisplayMode = null;

            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;

                for (DisplayMode current : modes) {
                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            } else {
                targetDisplayMode = new DisplayMode(width, height);
            }

            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
                return false;
            }

            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);

            System.out.println("Selected DisplayMode: " + targetDisplayMode.toString());

            resized();
            return true;
        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
        }

        return false;
    }

    public static void resized() {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    public static boolean wasResized() {
        return Display.wasResized();
    }

    public static void sync(int fps) {
        Display.sync(fps);
    }

    public static void update() {
        Display.update();
    }

    public static void destroy() {
        Display.destroy();
    }

    public static void vsync(boolean vsync) {
        Display.setVSyncEnabled(vsync);
    }

    public static void resizable(boolean resizable) {
        Display.setResizable(resizable);
    }

}
