package me.geakstr.engine.model.Cube;

import me.geakstr.engine.model.Model;

public class Cube extends Model {

    public static final float[] diffuse = { 0.64f, 0.64f, 0.64f };
    public static final float[] normals = { 0, 1, 0 };

    public Cube(int id, int x, int y, int z, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ) {
        super(id, x, y, z, rotX, rotY, rotZ, scaleX, scaleY, scaleZ);
    }

    public Cube(int id, int x, int y, int z, float rotX, float rotY, float rotZ) {
        this(id, x, y, z, rotX, rotY, rotZ, 1, 1, 1);
    }

    public Cube(int id, int x, int y, int z) {
        this(id, x, y, z, 0, 0, 0, 1, 1, 1);
    }

    public Cube(int id) {
        this(id, 0, 0, 0);
    }

}
