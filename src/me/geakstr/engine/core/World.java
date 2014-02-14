package me.geakstr.engine.core;

import me.geakstr.engine.model.Cube;

public class World {
    // world[length][height][width]; world[x][y][z]
    private int[][][] world;

    private int length, width, height;

    private static int cubeId;

    public World(int length, int height, int width, int cubeId) {
        this.length = length;
        this.height = height;
        this.width = width;
        this.cubeId = cubeId;
        this.world = new int[length][height][width];
    }

    public void gen() {
        for (int x = 0; x < length; x++) {
            for (int z = 0; z < width; z++) {
                for (int y = 0; y < height; y++) {
                    setCube(x, y, z);
                }
            }
        }
    }

    public void render(Frustum frustum, Shader shader) {
        RenderEngine.start(cubeId);
        for (int x = 0; x < length; x++) {
            for (int z = 0; z < width; z++) {
                for (int y = 0; y < height; y++) {
                    int id = get(x, y, z);
                    if (isRenderableCube(id, x, y, z, frustum)) {
                        RenderEngine.render(x, y, z, shader, new Cube.Type[]{ Cube.Type.TOP, Cube.Type.BOTTOM });
                    }
                }
            }
        }
        RenderEngine.end(cubeId);
    }

    public boolean isRenderableCube(int id, int x, int y, int z, Frustum frustum) {
        return id == cubeId && !isSurrounded(x, y, z) && frustum.checkCube(x, y, z, 1) >= 1;
    }

    public boolean isSurrounded(int x, int y, int z) {
        if (x == length - 1 || y == height - 1 || z == width - 1 || x == 0 || y == 0 || z == 0)
            return false;
        if (world[x - 1][y][z] != 0 && world[x + 1][y][z] != 0)
            if (world[x][y - 1][z] != 0 && world[x][y + 1][z] != 0)
                if (world[x][y][z - 1] != 0 && world[x][y][z + 1] != 0)
                    return true;
        return false;
    }

    public void set(int id, int x, int y, int z) {
        world[x][y][z] = id;
    }

    public void setCube(int x, int y, int z) {
        set(cubeId, x, y, z);
    }

    public void setAir(int x, int y, int z) {
        set(0, x, y, z);
    }

    public int get(int x, int y, int z) {
        return world[x][y][z];
    }

    public int[][][] getWorld() {
        return world;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static int getCubeId() { return cubeId; }
}
