package me.geakstr.engine.core;

import me.geakstr.engine.model.Cube.Cube;
import me.geakstr.engine.render.RenderCube;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    // world[length][height][width]; world[x][y][z]
    private static int[][][] world;

    private static int length, width, height;

    private static int cubeId;

    private World() {}

    public static void init(int _length, int _height, int _width, int _cubeId) {
        length = _length;
        height = _height;
        width = _width;
        cubeId = _cubeId;
        world = new int[length][height][width];
    }

    public static void gen() {
        Random rnd = new Random();
        for (int x = 0; x < length; x++) {
            for (int z = 0; z < width; z++) {
                for (int y = 0; y < height; y++) {
                    if (rnd.nextBoolean())
                        setCube(x, y, z);
                }
            }
        }
    }

    public static void renderPrepare() {
        RenderCube.buffering();
    }

    public static void render() {
        RenderCube.render();
    }

    public static void renderDispose() {
        RenderCube.dispose();
    }

    public static Cube.Side[] isRenderableSides(int id, int x, int y, int z) {
        Cube.Side[] sides = renderableSides(x, y, z);
    	
        return id == cubeId && sides.length > 0 ? sides : null;
    }

    public static Cube.Side[] renderableSides(int x, int y, int z) {
    	List<Cube.Side> sides = new ArrayList<Cube.Side>();
    	
    	if (x == 0 || world[x - 1][y][z] == 0)
    		sides.add(Cube.Side.LEFT);
    	if (x == length - 1 || world[x + 1][y][z] == 0)
    		sides.add(Cube.Side.RIGHT);
    	if (y == 0 || world[x][y - 1][z] == 0)
    		sides.add(Cube.Side.BOTTOM);
    	if (y == height - 1|| world[x][y + 1][z] == 0)
    		sides.add(Cube.Side.TOP);
    	if (z == 0 || world[x][y][z - 1] == 0)
    		sides.add(Cube.Side.FAR);
    	if (z == width - 1 || world[x][y][z + 1] == 0)
    		sides.add(Cube.Side.NEAR);

        Cube.Side[] ret = new Cube.Side[sides.size()];
    	sides.toArray(ret);
    	
        return ret;
    }

    public static void set(int id, int x, int y, int z) {
        world[x][y][z] = id;
    }

    public static void setCube(int x, int y, int z) {
        set(cubeId, x, y, z);
    }

    public static void setAir(int x, int y, int z) {
        set(0, x, y, z);
    }

    public static int get(int x, int y, int z) {
        return world[x][y][z];
    }

    public static int[][][] getWorld() {
        return world;
    }

    public static int getLength() {
        return length;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static int getCubeId() { return cubeId; }
}
