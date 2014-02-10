package me.geakstr.engine.core;

import me.geakstr.engine.model.Model;

public class World {
    // world[width][length][height]
    // world[x][z][y]
    private int[][][] world;

    // X
    private int length;

    // Z
    private int width;

    // Y
    private int height;

    public World(int width, int length, int height) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.world = new int[width][length][height];
    }

    public void setModelToMap(int model, int x, int y, int z) {
        world[x][z][y] = model;
    }

    public void setNullToMap(int x, int y, int z) {
        world[x][z][y] = 0;
    }

    public int getModelFromMap(int x, int y, int z) {
        return world[x][z][y];
    }

    public boolean isSurrounded(int x, int y, int z) {
        if (x == width - 1 || y == height - 1 || z == length - 1 || x == 0 || y == 0 || z == 0)
            return false;
        if (world[x - 1][z][y] != 0 && world[x + 1][z][y] != 0)
            if (world[x][z - 1][y] != 0 && world[x][z + 1][y] != 0)
                if (world[x][z][y - 1] != 0 && world[x][z][y + 1] != 0)
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
