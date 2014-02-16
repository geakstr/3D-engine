package me.geakstr.engine.core;

import me.geakstr.engine.math.Vector2f;
import me.geakstr.engine.math.Vector3f;
import me.geakstr.engine.model.*;
import me.geakstr.engine.render.RenderCube;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
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

    public static boolean isRenderableCube(int id, int x, int y, int z, Frustum frustum) {
        return id == cubeId && !isSurrounded(x, y, z) && frustum.checkCube(x, y, z, 1) >= 1;
    }

    public static boolean isRenderableCube(int id, int x, int y, int z) {
        return id == cubeId && !isSurrounded(x, y, z);
    }

    public static boolean isSurrounded(int x, int y, int z) {
        if (x == length - 1 || y == height - 1 || z == width - 1 || x == 0 || y == 0 || z == 0)
            return false;
        if (world[x - 1][y][z] != 0 && world[x + 1][y][z] != 0)
            if (world[x][y - 1][z] != 0 && world[x][y + 1][z] != 0)
                if (world[x][y][z - 1] != 0 && world[x][y][z + 1] != 0)
                    return true;
        return false;
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
