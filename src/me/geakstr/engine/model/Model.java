package me.geakstr.engine.model;

public class Model {
    private int id;

    private int x, y, z;
    private float rotX, rotY, rotZ;
    private float scaleX, scaleY, scaleZ;

    public Model(int id, int x, int y, int z, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    public Model(int id, int x, int y, int z, float rotX, float rotY, float rotZ) {
        this(id, x, y, z, rotX, rotY, rotZ, 1, 1, 1);
    }

    public Model(int id, int x, int y, int z) {
        this(id, x, y, z, 0, 0, 0, 1, 1, 1);
    }

    public Model(int id) {
        this(id, 0, 0, 0);
    }

    public Model clone(int x, int y, int z, float rotX, float rotY, float rotZ, float scaleX, float scaleY, float scaleZ) {
        return new Model(id, x, y, z, rotX, rotY, rotZ, scaleX, scaleY, scaleZ);
    }

    public Model clone(int x, int y, int z, float rotX, float rotY, float rotZ) {
        return clone(x, y, z, rotX, rotY, rotZ, scaleX, scaleY, scaleZ);
    }

    public Model clone(int x, int y, int z) {
        return clone(x, y, z, rotX, rotY, rotZ);
    }

    public Model clone(boolean clear) {
        return clear ? clone(0, 0, 0, 0, 0, 0, 1, 1, 1) : clone(x, y, z, rotX, rotY, rotZ, scaleX, scaleY, scaleZ);
    }

    public Model clone() {
        return clone(false);
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public float getRotX() {
        return rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public float getScaleZ() {
        return scaleZ;
    }

}
