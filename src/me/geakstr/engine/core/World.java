package me.geakstr.engine.core;


public class World {
    // world[length][height][width]; world[x][y][z]
    private int[][][] world;

    private int length, width, height;

    public World(int length, int height, int width) {
        this.length = length;
        this.height = height;
        this.width = width;
        this.world = new int[length][height][width];
    }

    public void setModelToMap(int model, int x, int y, int z) {
        world[x][y][z] = model;
    }

    public void setNullToMap(int x, int y, int z) {
        world[x][y][z] = 0;
    }

    public int getModelFromMap(int x, int y, int z) {
        return world[x][y][z];
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
}
