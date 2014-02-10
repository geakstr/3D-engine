package me.geakstr.engine.core;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

public class Game implements Runnable{
	private static Game instance;

	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	public static final boolean VSYNC_ENABLED = true;
	public static final boolean RESIZE_ENABLED = false;
	public static final boolean FULLSCREEN_ENABLED = false;
	public static final double FRAME_CAP = 60;

    public static String RES_DIR;

    public Game(String resDir) {
        RES_DIR = resDir;
        try {
            instance = this;
            Display.create();
            Display.setVSyncEnabled(VSYNC_ENABLED);
            Display.setResizable(RESIZE_ENABLED);
            setDisplayMode(WIDTH, HEIGHT, FULLSCREEN_ENABLED);
            run();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

	public Game(String resDir, int majorGL, int minorGL) {
        RES_DIR = resDir;
		try {
			instance = this;
			Display.create(new PixelFormat(), new ContextAttribs(majorGL, minorGL).withForwardCompatible(true).withProfileCore(true));
			Display.setVSyncEnabled(VSYNC_ENABLED);
			Display.setResizable(RESIZE_ENABLED);
			setDisplayMode(WIDTH, HEIGHT, FULLSCREEN_ENABLED);
			run();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
    }

	public void run() {

        init();

        int frames = 0;
		long frameCounter = 0;

		final double frameTime = Time.frameTime;

		long lastTime = Time.getTime();
		double unprocessedTime = 0;

        while (!Display.isCloseRequested()) {
        	boolean render = false;
        	long startTime = Time.getTime();
			long passedTime = startTime - lastTime;
			lastTime = startTime;
			unprocessedTime += passedTime / (double) Time.SECOND;
			frameCounter += passedTime;

			while (unprocessedTime > frameTime) {
				render = true;
				unprocessedTime -= frameTime;
				if (Display.isCloseRequested()) end();
				update();
				if (frameCounter > Time.SECOND) {
                    Display.setTitle("3D Engine [FPS: " + frames + "]");
					frames = 0;
					frameCounter = 0;
				}
			}
			if (render) {
				Display.update();
				render();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
        }
        end();
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

			instance.resized();
			return true;
		} catch (LWJGLException e) {
			System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
		}

		return false;
	}

	public static void end() {
		instance.dispose();
		instance = null;
		Display.destroy();
		System.exit(0);
	}

	public void init() {}

	public void update() {}

	public void render() {}

	public void resized() {}

	public void dispose() {}
}
