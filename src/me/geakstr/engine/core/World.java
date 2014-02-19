package me.geakstr.engine.core;

import me.geakstr.engine.model.Cube.Cube;
import me.geakstr.engine.model.Cube.CubeType;
import me.geakstr.engine.render.RenderCube;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL32.glDrawElementsBaseVertex;

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
        for (int x = 0; x < length; x++) {
            for (int z = 0; z < width; z++) {
                for (int y = 0; y < height; y++) {
                    setCube(x, y, z);
                }
            }
        }
    }

    public static void renderPrepare() {
        RenderCube.prepare();
    }

    public static void render(Shader shader) {
        RenderCube.render(shader);
    }

    public static void renderDispose() {
        RenderCube.dispose();
    }

    public static CubeType[] isRenderableSides(int id, int x, int y, int z) {
    	CubeType[] sides = renderableSides(x, y, z);
    	
        return id == cubeId && sides.length > 0 ? sides : null;
    }

    public static CubeType[] renderableSides(int x, int y, int z) {
    	List<CubeType> sides = new ArrayList<CubeType>();
    	
    	if (x == 0 || world[x - 1][y][z] == 0)
    		sides.add(CubeType.LEFT);
    	if (x == length - 1 || world[x + 1][y][z] == 0)
    		sides.add(CubeType.RIGHT);
    	if (y == 0 || world[x][y - 1][z] == 0)
    		sides.add(CubeType.BOTTOM);
    	if (y == height - 1|| world[x][y + 1][z] == 0)
    		sides.add(CubeType.TOP);
    	if (z == 0 || world[x][y][z - 1] == 0)
    		sides.add(CubeType.NEAR);
    	if (z == width - 1 || world[x][y][z + 1] == 0)
    		sides.add(CubeType.FAR);
    	
    	CubeType[] ret = new CubeType[sides.size()];
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
